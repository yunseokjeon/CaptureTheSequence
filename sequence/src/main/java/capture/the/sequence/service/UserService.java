package capture.the.sequence.service;

import capture.the.sequence.controller.PriceTableCategory;
import capture.the.sequence.dto.PriceDTO;
import capture.the.sequence.dto.UserDTO;
import capture.the.sequence.model.PriceEntity;
import capture.the.sequence.model.UserCategory;
import capture.the.sequence.model.UserEntity;
import capture.the.sequence.persistence.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.security.access.method.P;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final EntityManager entityManager;

    public UserEntity create(final UserEntity userEntity) {
        if (userEntity == null || userEntity.getEmail() == null) {
            throw new RuntimeException("Invalid arguments");
        }
        final String email = userEntity.getEmail();
        if (userRepository.existsByEmail(email)) {
            log.warn("Email already exists {}", email);
            throw new RuntimeException("Email already exists");
        }

        return userRepository.save(userEntity);
    }

    public UserEntity getByCredentials(final String email, final String password, final PasswordEncoder encoder) {
        final UserEntity originalUser = userRepository.findByEmail(email);

        // matches 메서드를 이용해 패스워드가 같은지 확인
        if (originalUser != null && encoder.matches(password, originalUser.getPassword())) {
            return originalUser;
        }
        return null;
    }

    public UserEntity getUserById(String id) {
        return userRepository.getById(id);
    }

    public List<UserDTO> getAllUserList() {
        List<UserEntity> allUserList = userRepository.findAllUserList();

        List<UserDTO> userDtoList = new ArrayList<>();
        for (UserEntity userEntity : allUserList) {
            if (userEntity.getUserCategory() != UserCategory.ADMIN) {
                UserDTO userDTO = new UserDTO();
                userDTO.setUsername(userEntity.getUsername());
                userDTO.setEmail(userEntity.getEmail());
                userDTO.setCreated_at(userEntity.getCreated_at());
                userDTO.setApproved(userEntity.isApproved());
                userDTO.setUserCategory(userEntity.getUserCategory());
                userDTO.setId(userEntity.getId());
                userDtoList.add(userDTO);
            }
        }
        return userDtoList;
    }

    public UserEntity activateAccount(String email) {
        UserEntity userEntity = userRepository.findByEmail(email);
        userEntity.setApproved(!userEntity.isApproved());
        entityManager.flush();
        entityManager.clear();
        userEntity = userRepository.findByEmail(email);
        return userEntity;
    }

    public List<PriceDTO> readExcel(String userId, MultipartFile file, PriceTableCategory priceTableCategory)
            throws IOException {
        UserEntity user = userRepository.getById(userId);
        try {
            List<PriceEntity> priceEntityList = getPriceEntityList(user, file, priceTableCategory);
            List<PriceDTO> priceDTOList = priceDtoToPriceEntity(priceEntityList);
            return priceDTOList;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw e;
        }
    }

    public List<PriceEntity> getPriceEntityList(UserEntity user, MultipartFile file, PriceTableCategory priceTableCategory)
            throws IOException {

        log.info("================================================");
        log.info(String.valueOf(user.getStockPriceList().size()));
        log.info("================================================");

        List<PriceEntity> priceEntityList = new ArrayList<>();

        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        if (!extension.equals("xlsx") && !extension.equals("xls")) {
            throw new IOException("You only have to upload a Excel file.");
        }

        Workbook workbook = null;

        if (extension.equals("xlsx")) {
            workbook = new XSSFWorkbook(file.getInputStream());
        } else if (extension.equals("xls")) {
            workbook = new HSSFWorkbook(file.getInputStream());
        }

        try {
            Sheet worksheet = workbook.getSheetAt(0);

            for (int i = 1; i < worksheet.getPhysicalNumberOfRows(); i++) {
                Row row = worksheet.getRow(i);
                PriceEntity priceEntity = PriceEntity.builder()
                        .marketDate(row.getCell(0).getDateCellValue()
                                .toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
                        .itemName(row.getCell(1).toString())
                        .startingPrice(row.getCell(2).getNumericCellValue())
                        .closingPrice(row.getCell(3).getNumericCellValue())
                        .user(user)
                        .build();
                priceEntityList.add(priceEntity);
            }

        } catch (Exception e) {
            throw e;
        }

        deleteDuplicatePriceItem(user, priceEntityList.get(0), priceTableCategory);
        if (priceTableCategory == PriceTableCategory.STOCK) {
            user.getStockPriceList().addAll(priceEntityList);
        } else if (priceTableCategory == PriceTableCategory.FUTURES) {
            user.getFuturesPriceList().addAll(priceEntityList);
        }

        log.info("================================================");
        log.info("flush and clear");
        entityManager.flush();
        entityManager.clear();
        log.info("================================================");

        log.info("================================================");
        log.info(user.getStockPriceList().get(0).getItemName());
        log.info(String.valueOf(user.getStockPriceList().size()));
        log.info("================================================");
        return priceEntityList;
    }

    public void deleteDuplicatePriceItem(UserEntity user, PriceEntity litmus, PriceTableCategory priceTableCategory) {
        if (priceTableCategory == PriceTableCategory.STOCK) {
            Iterator<PriceEntity> iterator = user.getStockPriceList().iterator();
            while (iterator.hasNext()) {
                PriceEntity price = (PriceEntity) iterator.next();
                if (price.getItemName() == litmus.getItemName()) {
                    iterator.remove();
                }
            }

        } else if (priceTableCategory == PriceTableCategory.FUTURES) {
            Iterator<PriceEntity> iterator = user.getFuturesPriceList().iterator();
            while (iterator.hasNext()) {
                PriceEntity price = (PriceEntity) iterator.next();
                if (price.getItemName() == litmus.getItemName()) {
                    iterator.remove();
                }
            }
        }
    }

    public List<PriceDTO> priceDtoToPriceEntity(List<PriceEntity> priceEntityList) {
        List<PriceDTO> priceDTOList = new ArrayList<>();
        for (PriceEntity priceEntity : priceEntityList) {
            PriceDTO priceDTO = PriceDTO.builder()
                    .marketDate(priceEntity.getMarketDate())
                    .itemName(priceEntity.getItemName())
                    .startingPrice(priceEntity.getStartingPrice())
                    .closingPrice(priceEntity.getClosingPrice())
                    .build();
            priceDTOList.add(priceDTO);
        }
        return priceDTOList;
    }


}

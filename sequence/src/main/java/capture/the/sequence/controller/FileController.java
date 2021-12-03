package capture.the.sequence.controller;

import capture.the.sequence.dto.PriceDTO;
import capture.the.sequence.dto.ResponseDTO;
import capture.the.sequence.model.UserCategory;
import capture.the.sequence.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
public class FileController {

    private final UserService userService;

    @PostMapping("/excel/read")
    public ResponseEntity<?> readExcel(@AuthenticationPrincipal String userId,
                                       @RequestParam("file") MultipartFile file) throws IOException {

        log.trace("readExcel");
        log.info("user information");
        log.info(userService.getUserById(userId).getEmail());

        if (userService.getUserById(userId).isApproved() == false) {
            ResponseDTO responseDTO = ResponseDTO.builder().error("You don't have authorization.").build();
            return ResponseEntity.badRequest().body(responseDTO);
        }

        List<PriceDTO> dataList = new ArrayList<>();

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
                PriceDTO priceDTO = PriceDTO.builder()
                        .marketDate(row.getCell(0).getDateCellValue()
                                .toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
                        .stockItem(row.getCell(1).toString())
                        .startingPrice(row.getCell(2).getNumericCellValue())
                        .closingPrice(row.getCell(3).getNumericCellValue())
                        .build();
                dataList.add(priceDTO);
            }
        } catch (Exception e) {

            ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();
            return ResponseEntity
                    .badRequest()
                    .body(responseDTO);
        }

        ResponseDTO<PriceDTO> response = ResponseDTO.<PriceDTO>builder().data(dataList).build();
        return ResponseEntity.ok(response);
    }
}

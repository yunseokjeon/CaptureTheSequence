package capture.the.sequence.controller;

import capture.the.sequence.dto.PriceDTO;
import capture.the.sequence.dto.ResponseDTO;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
                                       @RequestParam("file") MultipartFile file,
                                       @RequestParam("priceTableCategory") PriceTableCategory priceTableCategory) throws IOException {

        if (userService.getUserById(userId).isApproved() == false) {
            ResponseDTO responseDTO = ResponseDTO.builder().error("You don't have authorization.").build();
            return ResponseEntity.badRequest().body(responseDTO);
        }

        try {
            List<PriceDTO> dataList = userService.readExcel(userId, file, priceTableCategory);
            ResponseDTO<PriceDTO> response = ResponseDTO.<PriceDTO>builder().data(dataList).build();
            return ResponseEntity.ok(response);
        } catch (Exception e) {

            ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();
            return ResponseEntity
                    .badRequest()
                    .body(responseDTO);
        }


    }
}

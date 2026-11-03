package uz.davrbank.creditaccountsservice.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uz.davrbank.creditaccountsservice.aggregate.request.CreditAccountRequestDTO;
import uz.davrbank.creditaccountsservice.aggregate.request.CreditRequestPageable;
import uz.davrbank.creditaccountsservice.aggregate.response.CreditAccountResDataDTO;
import uz.davrbank.creditaccountsservice.aggregate.response.ResponsePaginationDTO;
import uz.davrbank.creditaccountsservice.cloud.interfaces.MinioInterface;
import uz.davrbank.creditaccountsservice.service.CreditAccountInterface;
import uz.davrbank.creditaccountsservice.service.ReportServiceInterface;
import uz.davrbank.creditaccountsservice.utils.ExcelStyles;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ReportServiceImpl implements ReportServiceInterface {

    private final CreditAccountInterface creditAccountInterface;
    private final MinioInterface minioInterface;
    private static final int PAGE_SIZE = 20000;

    public ReportServiceImpl(CreditAccountInterface creditAccountInterface,
                             MinioInterface minioInterface) {
        this.creditAccountInterface = creditAccountInterface;
        this.minioInterface = minioInterface;
    }

    @Override
    public ResponseEntity<?> uploadReportToMinio(CreditAccountRequestDTO creditAccountRequestDTO, String userName) {

        log.info("Report upload to Minio - creditAcount: {}, userName:{}",
                creditAccountRequestDTO, userName);

        try {

            List<CreditAccountResDataDTO> allData = getAllData(creditAccountRequestDTO);

            if (allData.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No data found for given parameters");
            }

            byte[] excelBytes = generateExcel(allData,userName);


            String fileUrl = minioInterface.createExcelFileInMinio(
                    creditAccountRequestDTO, excelBytes
            );

            log.info("Excel uploaded to Minio: {}", fileUrl);


            return buildExcelResponse(excelBytes, fileUrl);

        } catch (Exception e) {
            log.error("Upload report error: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError()
                    .body("Internal error: " + e.getMessage());
        }
    }

    private List<CreditAccountResDataDTO> getAllData(CreditAccountRequestDTO request) {

        List<CreditAccountResDataDTO> result = new ArrayList<>();
        int page = 1;
        boolean last;

        log.info("Start pagination data fetch...");

        do {
            CreditRequestPageable pageable = CreditRequestPageable.builder()
                    .date(request.getDate())
                    .branch(request.getBranch())
                    .state(request.getState())
                    .valType(request.getValType())
                    .account(request.getAccount())
                    .pageNumber(page)
                    .pageSize(PAGE_SIZE)
                    .build();

            ResponsePaginationDTO response = creditAccountInterface
                    .getAllCreditAccountsPageable(pageable);

            if (response.getData() == null || response.getData().isEmpty()) break;

            result.addAll(response.getData());
            last = Boolean.TRUE.equals(response.getLast());

            log.info("Fetched page {}, count: {}", page, response.getData().size());
            page++;

        } while (!last);

        log.info("Total data fetched: {}", result.size());
        return result;
    }

    private byte[] generateExcel(List<CreditAccountResDataDTO> data, String userName) throws IOException {

        try (SXSSFWorkbook workbook = new SXSSFWorkbook(500);
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            SXSSFSheet sheet = workbook.createSheet("Credit Accounts Report");
            sheet.trackAllColumnsForAutoSizing();

            CellStyle headerStyle = ExcelStyles.createHeaderStyle(workbook);
            CellStyle dataStyle = ExcelStyles.createDataStyle(workbook);
            CellStyle amountStyle = ExcelStyles.createAmountStyle(workbook);


            CellStyle titleStyle = workbook.createCellStyle();
            Font titleFont = workbook.createFont();
            titleFont.setBold(true);
            titleStyle.setFont(titleFont);

            int rowIndex = 0;

            Row row = sheet.createRow(rowIndex++);
            row.createCell(0).setCellValue("Отчет:");
            row.createCell(1).setCellValue("Отчет по счетам кредита");
            row.getCell(0).setCellStyle(titleStyle);
            row.getCell(1).setCellStyle(dataStyle);

            row = sheet.createRow(rowIndex++);
            row.createCell(0).setCellValue("Отделение:");
            row.createCell(1).setCellValue("ЧАКБ «Davr bank» — Департамент ИТ");
            row.getCell(0).setCellStyle(titleStyle);
            row.getCell(1).setCellStyle(dataStyle);

            row = sheet.createRow(rowIndex++);
            row.createCell(0).setCellValue("Загружено:");
            row.createCell(1).setCellValue(userName != null ? userName : "anonymous");
            row.getCell(0).setCellStyle(titleStyle);
            row.getCell(1).setCellStyle(dataStyle);

            String formattedDateTime = ZonedDateTime.now(ZoneId.of("Asia/Tashkent"))
                    .format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"));

            row = sheet.createRow(rowIndex++);
            row.createCell(0).setCellValue("Дата:");
            row.createCell(1).setCellValue(formattedDateTime);
            row.getCell(0).setCellStyle(titleStyle);
            row.getCell(1).setCellStyle(dataStyle);

            rowIndex++;


            String[] headers = {
                    "Филиал (МФО)",
                    "ID анкеты",
                    "Счет",
                    "ID типа счёта",
                    "Наименование типа счёта",
                    "Исходящее сальдо",
                    "Статус",
                    "Наименование статуса"
            };

            Row headerRow = sheet.createRow(rowIndex++);
            headerRow.setHeightInPoints(30);

            for (int i = 0; i < headers.length; i++) {
                Cell c = headerRow.createCell(i);
                c.setCellValue(headers[i]);
                c.setCellStyle(headerStyle);
            }

            for (CreditAccountResDataDTO item : data) {
                Row dataRow = sheet.createRow(rowIndex++);
                int col = 0;

                ExcelStyles.setCellValue(dataRow, col++, item.getBranch(), dataStyle);
                ExcelStyles.setCellValue(dataRow, col++, item.getAnketaId(), dataStyle);
                ExcelStyles.setCellValue(dataRow, col++, item.getAccount(), dataStyle);
                ExcelStyles.setCellValue(dataRow, col++, item.getAccTypeId(), dataStyle);
                ExcelStyles.setCellValue(dataRow, col++, item.getAccTypeName(), dataStyle);

                Cell saldoCell = dataRow.createCell(col++);
                saldoCell.setCellValue(item.getSaldoOut() != null ? item.getSaldoOut() : 0);
                saldoCell.setCellStyle(amountStyle);

                ExcelStyles.setCellValue(dataRow, col++, item.getState(), dataStyle);
                ExcelStyles.setCellValue(dataRow, col, item.getStateName(), dataStyle);
            }

            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(baos);
            workbook.dispose();
            return baos.toByteArray();
        }
    }

    private ResponseEntity<ByteArrayResource> buildExcelResponse(byte[] bytes, String url) {
        String fileName = url.substring(url.lastIndexOf('/') + 1);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                .contentType(MediaType.parseMediaType(
                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .contentLength(bytes.length)
                .body(new ByteArrayResource(bytes));
    }
}


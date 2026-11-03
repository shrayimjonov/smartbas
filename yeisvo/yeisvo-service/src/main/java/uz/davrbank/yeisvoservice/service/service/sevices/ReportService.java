package uz.davrbank.yeisvoservice.service.service.sevices;

import com.fasterxml.jackson.databind.ObjectMapper;
import uz.davrbank.yeisvoservice.Feign.YeisvoHubClient;
import uz.davrbank.yeisvoservice.aggregate.MinioProperty;
import uz.davrbank.yeisvoservice.aggregate.dto.response.report.*;
import uz.davrbank.yeisvoservice.exception.controller.GlobalExceptionMessage;
import uz.davrbank.yeisvoservice.service.service.interfaces.ReportInterface;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;

import jakarta.servlet.http.HttpServletRequest;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReportService implements ReportInterface {
    @Autowired
    private final HttpServletRequest httpRequest;
    private final YeisvoHubClient yeisvoHubClient;
    private final MinioClient minioClient;
    private final MinioProperty minioProperty;


    @Override
    public ResponseDTO<List<?>> getAllReports(ReportAllRequest request) {
        int transCode = request.getTransType().getCode();

        ResponseDTO<List<?>> allReports =
                yeisvoHubClient.getAllReports(transCode, request.getStartDate(), request.getEndDate());

        if (allReports == null || allReports.getData() == null) {
            allReports = new ResponseDTO<>();
            allReports.setError("Report list is empty");
            return allReports;
        }
        log.info("getAllReports ----> size = {}", allReports.getData().size());
        return allReports;
    }


    @Override
    public ResponseDTO<List<Object>> getPagedReports(ReportPagedRequest request) {
        ResponseDTO<List<Object>> allReports = yeisvoHubClient.getPagedReports(request.getTransType(), request.getStartDate(), request.getEndDate(), request.getPageNumber(), request.getPageSize());
        if (allReports == null || allReports.getData() == null) {
            allReports = new ResponseDTO<>();
            allReports.setError("Report list is empty");
            return allReports;
        }
        return allReports;
    }


    /**
     * 1️⃣ Excel faylni yaratish va to‘ldirish (PRITTOK 1)
     * 2️⃣ MinIO ga yuklash
     * 3️⃣ ResponseEntity qaytarish
     */
    @Override
    public DownloadDTO generateAndUploadExcel(ReportAllRequest data) throws IOException {
        String username = httpRequest.getHeader("username");

        if (data == null) {
            log.error("Report request data is null");
            throw new GlobalExceptionMessage(
                    400,
                    "Report request data cannot be null",
                    HttpStatus.BAD_REQUEST
            );
        }
        if (username == null || username.isEmpty()) {
            log.error("Username is null or empty");
            throw new GlobalExceptionMessage(
                    400,
                    "Username cannot be null or empty",
                    HttpStatus.BAD_REQUEST
            );
        }
        ObjectMapper mapper = new ObjectMapper();
        byte[] excelBytes = new byte[0];

        int transCode = data.getTransType().getCode();

        ResponseDTO<List<?>> allReportsRaw = yeisvoHubClient.getAllReports(
                transCode, data.getStartDate(), data.getEndDate()
        );

        if (allReportsRaw == null || allReportsRaw.getData() == null) {
            throw new GlobalExceptionMessage("Report list is empty");
        }


        if (transCode == 1) {
            List<ResponseDataDTO> reports = allReportsRaw.getData().stream()
                    .map(obj -> mapper.convertValue(obj, ResponseDataDTO.class))
                    .collect(Collectors.toList());

            excelBytes = fillExcelTemplate(reports, username);
        } else if (transCode == 2) {
            List<OttokResponse> reports = allReportsRaw.getData().stream()
                    .map(obj -> mapper.convertValue(obj, OttokResponse.class))
                    .collect(Collectors.toList());

            excelBytes = fillOttokExcelTemplate(reports, username);
        } else {
            throw new GlobalExceptionMessage("Unsupported transaction type: " + transCode);
        }

        String fileUrl = uploadToMinio(excelBytes, data);

        return DownloadDTO.builder()
                .base64(Base64.getEncoder().encodeToString(excelBytes))
                .fileName(fileUrl.substring(fileUrl.lastIndexOf('/') + 1))
                .build();
    }

    private byte[] fillExcelTemplate(List<ResponseDataDTO> data, String username) throws IOException {
        Workbook workbook = getWorkbookTemplate();
        Sheet sheet = workbook.getSheetAt(0);

        fillHeaderInfo(1, sheet, workbook, username);

        int rowIndex = 12;
        int counter = 1;

        for (ResponseDataDTO dto : data) {
            Row row = sheet.createRow(rowIndex++);

            row.createCell(0).setCellValue(counter++);
            row.createCell(1).setCellValue(dto.getDATE_OPER());
            row.createCell(2).setCellValue(dto.getINN_CLIENT());
            row.createCell(3).setCellValue(dto.getNAME_CLIENT());
            row.createCell(4).setCellValue(dto.getIDN());
            row.createCell(5).setCellValue(dto.getOKED());
            row.createCell(6).setCellValue(dto.getOKONX());
            row.createCell(7).setCellValue(dto.getKOD_REGION());
            row.createCell(8).setCellValue(dto.getKOD_STRANA());
            row.createCell(9).setCellValue(dto.getKOD_VAL());

            // === SUMMA STYLE ===
            CellStyle amountStyle = workbook.createCellStyle();
            DataFormat format = workbook.createDataFormat();
            amountStyle.setDataFormat(format.getFormat("#,##0.00"));
            amountStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            amountStyle.setAlignment(HorizontalAlignment.CENTER);

            setAmountCell(row, 10, dto.getSUMMA_7(), amountStyle);
            setAmountCell(row, 11, dto.getSUMMA_8(), amountStyle);
            setAmountCell(row, 12, dto.getSUMMA_81(), amountStyle);
            setAmountCell(row, 13, dto.getSUMMA_82(), amountStyle);
            setAmountCell(row, 14, dto.getSUMMA_83(), amountStyle);
            setAmountCell(row, 15, dto.getSUMMA_84(), amountStyle);
            setAmountCell(row, 16, dto.getSUMMA_9(), amountStyle);
            setAmountCell(row, 17, dto.getSUMMA_10(), amountStyle);
            setAmountCell(row, 18, dto.getSUMMA_11(), amountStyle);
            setAmountCell(row, 19, dto.getSUMMA_12(), amountStyle);
            setAmountCell(row, 20, dto.getSUMMA_13(), amountStyle);
            setAmountCell(row, 21, dto.getSUMMA_14(), amountStyle);
            setAmountCell(row, 22, dto.getSUMMA_15(), amountStyle);
            setAmountCell(row, 23, dto.getSUMMA_16(), amountStyle);
            setAmountCell(row, 24, dto.getSUMMA_17(), amountStyle);
            setAmountCell(row, 25, dto.getSUMMA_18(), amountStyle);
            setAmountCell(row, 26, dto.getSUMMA_19(), amountStyle);
            setAmountCell(row, 27, dto.getSUMMA_20(), amountStyle);

            row.createCell(28).setCellValue(dto.getVID_OPER());
            row.createCell(29).setCellValue(dto.getSUMMA_CHECK_1());
            row.createCell(30).setCellValue(dto.getSUMMA_CHECK_2());
            row.createCell(31).setCellValue(String.valueOf(dto.getGENERAL_ID()));
            row.createCell(32).setCellValue(dto.getACC_CL());
            row.createCell(33).setCellValue(dto.getACC_CO());
            row.createCell(34).setCellValue(dto.getTRANS_KOD1());
            row.createCell(35).setCellValue(dto.getTRANS_KOD2());
            row.createCell(36).setCellValue(dto.getPURPOSE());
            row.createCell(37).setCellValue("");
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();

        return outputStream.toByteArray();
    }

    private byte[] fillOttokExcelTemplate(List<OttokResponse> data, String username) throws IOException {
        Workbook workbook = getWorkbookOttok();
        Sheet sheet = workbook.getSheetAt(0);

        fillHeaderInfo(2, sheet, workbook, username);

        int rowIndex = 12;
        int counter = 1;

        for (OttokResponse dto : data) {
            Row row = sheet.createRow(rowIndex++);
            row.createCell(0).setCellValue(counter++);
            row.createCell(1).setCellValue(dto.getDATE_OPER());
            row.createCell(2).setCellValue(dto.getINN_CLIENT());
            row.createCell(3).setCellValue(dto.getNAME_CLIENT());
            row.createCell(4).setCellValue(dto.getIDN());
            row.createCell(5).setCellValue(dto.getOKED());
            row.createCell(6).setCellValue(dto.getOKONX());
            row.createCell(7).setCellValue(dto.getKOD_REGION());
            row.createCell(8).setCellValue(dto.getKOD_STRANA());
            row.createCell(9).setCellValue(dto.getKOD_OFFSHORE1());
            row.createCell(10).setCellValue(dto.getKOD_OFFSHORE2());
            row.createCell(11).setCellValue(dto.getKOD_VAL());
            // === SUMMA STYLE ===
            CellStyle amountStyle = workbook.createCellStyle();
            DataFormat format = workbook.createDataFormat();
            amountStyle.setDataFormat(format.getFormat("#,##0.00"));
            amountStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            amountStyle.setAlignment(HorizontalAlignment.CENTER);

            setAmountCell(row, 12, dto.getSUMMA_7(), amountStyle);
            setAmountCell(row, 13, dto.getSUMMA_8(), amountStyle);
            setAmountCell(row, 14, dto.getSUMMA_811(), amountStyle);
            setAmountCell(row, 15, dto.getSUMMA_812(), amountStyle);
            setAmountCell(row, 16, dto.getSUMMA_821(), amountStyle);
            setAmountCell(row, 17, dto.getSUMMA_822(), amountStyle);
            setAmountCell(row, 18, dto.getSUMMA_823(), amountStyle);
            setAmountCell(row, 19, dto.getSUMMA_9(), amountStyle);
            setAmountCell(row, 20, dto.getSUMMA_10(), amountStyle);
            setAmountCell(row, 21, dto.getSUMMA_11(), amountStyle);
            setAmountCell(row, 22, dto.getSUMMA_12(), amountStyle);
            setAmountCell(row, 23, dto.getSUMMA_13(), amountStyle);
            setAmountCell(row, 24, dto.getSUMMA_14(), amountStyle);
            setAmountCell(row, 25, dto.getSUMMA_15(), amountStyle);
            setAmountCell(row, 26, dto.getSUMMA_16(), amountStyle);
            setAmountCell(row, 27, dto.getSUMMA_17(), amountStyle);
            setAmountCell(row, 28, dto.getSUMMA_18(), amountStyle);
            setAmountCell(row, 29, dto.getSUMMA_19(), amountStyle);
            setAmountCell(row, 30, dto.getSUMMA_20(), amountStyle);
            setAmountCell(row, 31, dto.getSUMMA_21(), amountStyle);

            row.createCell(32).setCellValue(dto.getVID_OPER());
            row.createCell(33).setCellValue(dto.getSUMMA_CHECK_1());
            row.createCell(34).setCellValue(dto.getSUMMA_CHECK_2());
            row.createCell(35).setCellValue(String.valueOf(dto.getGENERAL_ID()));
            row.createCell(36).setCellValue(dto.getACC_CL());
            row.createCell(37).setCellValue(dto.getACC_CO());
            row.createCell(38).setCellValue(dto.getTRANS_KOD1());
            row.createCell(39).setCellValue(dto.getPURPOSE());
            row.createCell(40).setCellValue("");
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();

        return outputStream.toByteArray();
    }

    private void fillHeaderInfo(int transType, Sheet sheet, Workbook workbook, String username) {
        CellStyle boldStyle = workbook.createCellStyle();
        Font boldFont = workbook.createFont();
        boldFont.setBold(true);
        boldStyle.setFont(boldFont);

        CellStyle normalStyle = workbook.createCellStyle();
        Font normalFont = workbook.createFont();
        normalStyle.setFont(normalFont);

        int rowIndex = 2;

        Row row1 = sheet.createRow(rowIndex++);
        row1.createCell(0).setCellValue("Отчет:");
        row1.getCell(0).setCellStyle(boldStyle);
        if (transType == 1) {
            row1.createCell(1).setCellValue("Информация о поступлениях средств в иностранной валюте");
        }
        if (transType == 2) {
            row1.createCell(1).setCellValue("Информация о расходах средств в иностранной валюте");
        }
        row1.getCell(1).setCellStyle(normalStyle);

        Row row2 = sheet.createRow(rowIndex++);
        row2.createCell(0).setCellValue("Отделение:");
        row2.getCell(0).setCellStyle(boldStyle);
        row2.createCell(1).setCellValue("ЧАКБ «Davr bank» — Департамент ИТ, Отдел интеграции");
        row2.getCell(1).setCellStyle(normalStyle);

        Row row3 = sheet.createRow(rowIndex++);
        row3.createCell(0).setCellValue("Загружено:");
        row3.getCell(0).setCellStyle(boldStyle);
        row3.createCell(1).setCellValue(username != null ? username : "anonymous");
        row3.getCell(1).setCellStyle(normalStyle);

        String formattedDateTime = ZonedDateTime.now(ZoneId.of("Asia/Tashkent"))
                .format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"));

        Row row4 = sheet.createRow(rowIndex++);
        row4.createCell(0).setCellValue("Дата загрузки:");
        row4.getCell(0).setCellStyle(boldStyle);
        row4.createCell(1).setCellValue(formattedDateTime);
        row4.getCell(1).setCellStyle(normalStyle);

        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);
    }


    private void setAmountCell(Row row, int cellIndex, Long value, CellStyle style) {
        Cell cell = row.createCell(cellIndex);

        if (value == null) {
            cell.setBlank();
            return;
        }
        if (value == 0L) {
            cell.setCellValue("0");
        } else {
            double dividedValue = value / 100.0;
            cell.setCellValue(dividedValue);
            cell.setCellStyle(style);
        }
    }


    /**
     * 3. MinIO ga yuklash
     */
    private String uploadToMinio(byte[] excelBytes, ReportAllRequest data) {
        if (excelBytes == null) throw new GlobalExceptionMessage("Excel data is null");
        log.info("Saving excel to MinIO, startDate: {}, endDate: {}, cadastreType: {}, state: {}",
                data.getStartDate(), data.getEndDate(), data.getTransType());


        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Asia/Tashkent"));
        String fileName;
        int transCode = data.getTransType().getCode();
        if (transCode == 1) {
            fileName = String.format(
                    "Prittok_from_%s_to_%s_S%d.xlsx",
                    data.getStartDate(), data.getEndDate(), transCode);
        } else if (transCode == 2) {
            fileName = String.format(
                    "Ottok_from_%s_to_%s_S%d.xlsx",
                    data.getStartDate(), data.getEndDate(), transCode);
        } else {
            throw new GlobalExceptionMessage("Transtype is not supported");
        }
        String year = String.valueOf(now.getYear());
        String month = String.format("%02d", now.getMonthValue());
        String objectName = String.format("reports/yeisvo/swift/%s/%s/%s", year, month, fileName);

        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(excelBytes)) {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(minioProperty.getBucket())
                            .object(objectName)
                            .stream(inputStream, excelBytes.length, -1)
                            .contentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                            .build()
            );
            log.info("✅ Excel uploaded to MinIO: {}", objectName);

            return String.format("%s/%s/%s", minioProperty.getUrl(), minioProperty.getBucket(), objectName);
        } catch (Exception e) {
            log.error("❌ Error uploading Excel to MinIO: {}", e.getMessage(), e);
            throw new GlobalExceptionMessage(e.getMessage());
        }
    }

    public Workbook getWorkbookTemplate() throws IOException {
        ClassPathResource resource = new ClassPathResource("templates/template.xlsx");
        try {
            InputStream inputStream = resource.getInputStream();
            return new XSSFWorkbook(inputStream);
        } catch (Exception e) {
            throw new GlobalExceptionMessage("Problem with streaming the file");
        }
    }

    public Workbook getWorkbookOttok() throws IOException {
        ClassPathResource resource = new ClassPathResource("templates/ottok.xlsx");
        try {
            InputStream inputStream = resource.getInputStream();
            return new XSSFWorkbook(inputStream);
        } catch (Exception e) {
            throw new GlobalExceptionMessage("Problem with streaming the file");
        }
    }
}


package uz.davrbank.zalogipatekaservice.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uz.davrbank.zalogipatekaservice.aggregate.dto.ErrorResponseDto;
import uz.davrbank.zalogipatekaservice.aggregate.dto.request.ZalogIpotekaRequestParam;
import uz.davrbank.zalogipatekaservice.aggregate.dto.response.ResponseDataDTO;
import uz.davrbank.zalogipatekaservice.aggregate.dto.response.ResponsePaginationDTO;
import uz.davrbank.zalogipatekaservice.cloud.interfaces.MinioInterface;
import uz.davrbank.zalogipatekaservice.service.cases.ReportService;
import uz.davrbank.zalogipatekaservice.service.cases.ZalogIpotekaService;
import uz.davrbank.zalogipatekaservice.utils.ExcelStyles;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ReportServiceImpl implements ReportService {

    private static final Logger logger = LoggerFactory.getLogger(ReportServiceImpl.class);
    private final ZalogIpotekaService zalogIpotekaService;
    private final MinioInterface minioInterface;
    private static final int PAGE_SIZE = 50000;


    public ReportServiceImpl(ZalogIpotekaService zalogIpotekaService,
                             MinioInterface minioInterface) {
        this.zalogIpotekaService = zalogIpotekaService;
        this.minioInterface = minioInterface;

    }
    @Override
    public ResponseEntity<?> uploadReportToMinio(String startDate, String endDate,
                                                 Integer cadastreType, Integer state,
                                                 String branch, Integer loanId,
                                                 String clientId, String pinfl,
                                                 String clientName, String cadastreNum,
                                                 String userName) {
        logger.info("REQUEST: downloadReport - startDate: {}, endDate: {}, cadastreType: {}, state={}, branch={}, loanId={}, clientId={}, pinfl={}, clientName={}, cadastreNum={}",
                startDate, endDate, cadastreType, state, branch, loanId, clientId, pinfl, clientName, cadastreNum);

        try {

            if (state == null) {
                return ResponseEntity.badRequest()
                        .body(new ErrorResponseDto(400, "The 'state' parameter is required."));
            }

            if (cadastreType == null) {
                return ResponseEntity.badRequest()
                        .body(new ErrorResponseDto(400, "The 'cadastreType' parameter is required."));
            }

            boolean startProvided = startDate != null && !startDate.isEmpty();
            boolean endProvided = endDate != null && !endDate.isEmpty();

            if (startProvided != endProvided) {
                return ResponseEntity.badRequest()
                        .body(new ErrorResponseDto(400, "Both 'startDate' and 'endDate' must be provided together."));
            }

            if (startProvided && endProvided && startDate.equals(endDate)) {
                return ResponseEntity.badRequest()
                        .body(new ErrorResponseDto(400, "'startDate' and 'endDate' cannot be the same."));
            }

            ZalogIpotekaRequestParam requestParam = new ZalogIpotekaRequestParam();
            requestParam.setCadastreType(cadastreType);
            requestParam.setState(state);
            requestParam.setStartDate(startProvided ? startDate : null);
            requestParam.setEndDate(endProvided ? endDate : null);
            requestParam.setBranch(branch);
            requestParam.setLoanId(loanId);
            requestParam.setClientId(clientId);
            requestParam.setPinfl(pinfl);
            requestParam.setClientName(clientName);
            requestParam.setCadastreNum(cadastreNum);

            List<ResponseDataDTO> allData = getAllData(requestParam);
            if (allData.isEmpty()) {
                String msg = (startProvided && endProvided)
                        ? "No data found for the given date range."
                        : "No data found for the given parameters.";
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponseDto(404, msg));
            }


            byte[] excelBytes = generateExcel(allData, userName);


            String fileUrl = minioInterface.createExcelFileInMinio(
                    startDate, endDate, cadastreType, state,
                    branch, loanId, clientId, pinfl, clientName, cadastreNum,
                    excelBytes
            );

            logger.info("✅ Excel file uploaded to MinIO: {}", fileUrl);

            return buildExcelResponse(excelBytes, fileUrl);

        } catch (Exception e) {
            logger.error("❌ ERROR in uploadReport: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError()
                    .body(new ErrorResponseDto(500, e.getMessage() != null ? e.getMessage() : "Internal server error"));
        }
    }


    private List<ResponseDataDTO> getAllData(ZalogIpotekaRequestParam requestParam) {
        logger.info("STEP 1.1: Starting paginated data fetching – startDate: {}, endDate: {}, " +
                        "cadastreType={}, state={}, branch={}, loanId={}, clientId={}, pinfl={}, " +
                        "clientName={}, cadastreNum={}",
                requestParam.getStartDate(), requestParam.getEndDate(),
                requestParam.getCadastreType(), requestParam.getState(),
                requestParam.getBranch(), requestParam.getLoanId(),
                requestParam.getClientId(), requestParam.getPinfl(),
                requestParam.getClientName(), requestParam.getCadastreNum());

        List<ResponseDataDTO> result = new ArrayList<>();
        int page = 1;
        int totalFetched = 0;
        final int MAX_PAGE_LIMIT = 30000;
        boolean lastPage = false;

        do {
            ZalogIpotekaRequestParam req = new ZalogIpotekaRequestParam();
            req.setStartDate(requestParam.getStartDate());
            req.setEndDate(requestParam.getEndDate());
            req.setCadastreType(requestParam.getCadastreType());
            req.setState(requestParam.getState());
            req.setBranch(requestParam.getBranch());
            req.setLoanId(requestParam.getLoanId());
            req.setClientId(requestParam.getClientId());
            req.setPinfl(requestParam.getPinfl());
            req.setClientName(requestParam.getClientName());
            req.setCadastreNum(requestParam.getCadastreNum());
            req.setPage(page);
            req.setSize(PAGE_SIZE);

            ResponsePaginationDTO resp;
            try {
                resp = zalogIpotekaService.getPageable(req);
            } catch (Exception e) {
                logger.error("STEP 1.1: FAILED – Error fetching page {}: {}", page, e.getMessage(), e);
                throw new RuntimeException("Failed to fetch page " + page, e);
            }

            if (resp.getData() == null || resp.getData().isEmpty()) {
                logger.warn("STEP 1.1: Page {} returned empty data, stopping pagination", page);
                break;
            }

            result.addAll(resp.getData());
            totalFetched += resp.getData().size();
            logger.debug("Page {} fetched, records added: {}, total so far: {}", page, resp.getData().size(), totalFetched);

            lastPage = Boolean.TRUE.equals(resp.getLast());

            if (page >= MAX_PAGE_LIMIT) {
                logger.warn("STEP 1.1: Reached maximum page limit ({}) – stopping pagination to prevent infinite loop", MAX_PAGE_LIMIT);
                break;
            }

            page++;
        } while (!lastPage);

        logger.info("STEP 1.1: SUCCESS – Fetched {} records across {} pages", result.size(), page - 1);
        return result;
    }


    private byte[] generateExcel(List<ResponseDataDTO> data, String userName) throws IOException {
        try (SXSSFWorkbook workbook = new SXSSFWorkbook(50000);
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            SXSSFSheet sheet = workbook.createSheet("ZalogIpoteka Report");
            sheet.trackAllColumnsForAutoSizing();

            CellStyle headerStyle = ExcelStyles.createHeaderStyle(workbook);
            CellStyle dataStyle = ExcelStyles.createDataStyle(workbook);
            CellStyle amountStyle = ExcelStyles.createAmountStyle(workbook);

            CellStyle titleStyle = workbook.createCellStyle();
            Font titleFont = workbook.createFont();
            titleFont.setBold(true);
            titleStyle.setFont(titleFont);

            int rowIndex = 1;

            Row row = sheet.createRow(rowIndex++);
            row.createCell(0).setCellValue("Отчет:");
            row.createCell(1).setCellValue("Отчёт о залогах по ипотечным кредитам");
            row.getCell(0).setCellStyle(titleStyle);
            row.getCell(1).setCellStyle(dataStyle);

            row = sheet.createRow(rowIndex++);
            row.createCell(0).setCellValue("Отделение:");
            row.createCell(1).setCellValue("ЧАКБ «Davr bank» — Департамент ИТ, Отдел интеграции");
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
                    "ИД Обеспечения", "Филиал", "ИД анкеты", "Cсудный счет", "ИД клиента (Заемщик)", "ФИО (Заемщик)",
                    "ПИНФЛ, ИНН (Заемщик)","ИД клиента (Залогодатель)","ФИО (Залогодатель)","ПИНФЛ, ИНН (Залогодатель)", "Адрес NCI клиенты",
                    "Номер сертификата", "Дата сертификата","Тип кадастра",
                    "Кадастровый номер", "Номер реестра", "Площадь(m²)", "Тип собственности", "Кадастровая сумма",
                    "ИНН оценщика", "Дата документа", "Тип обеспечения", "Адрес обеспечения","Сумма залога", "Код валюты", "Статус анкеты"
            };
            // Header row (Russian)
            Row headerRow = sheet.createRow(rowIndex++);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
                headerRow.setHeightInPoints(30);
            }

            for (ResponseDataDTO item : data) {
                Row dataRow = sheet.createRow(rowIndex++);
                int col = 0;

                ExcelStyles.setCellValue(dataRow, col++, item.getPk(), dataStyle);
                ExcelStyles.setCellValue(dataRow, col++, nullToEmpty(item.getBranch()), dataStyle);
                ExcelStyles.setCellValue(dataRow, col++, item.getIdAnketa(), dataStyle);
                ExcelStyles.setCellValue(dataRow, col++, nullToEmpty(item.getAccount()), dataStyle);

                // --- NEW FIELDS (ZAEM – borrower) ---
                ExcelStyles.setCellValue(dataRow, col++, nullToEmpty(item.getIdClientZaem()), dataStyle);
                ExcelStyles.setCellValue(dataRow, col++, nullToEmpty(item.getFioZaem()), dataStyle);
                ExcelStyles.setCellValue(dataRow, col++, nullToEmpty(item.getPinflInnZaem()), dataStyle);

                // --- NEW FIELDS (ZALOG – pledge owner) ---
                ExcelStyles.setCellValue(dataRow, col++, nullToEmpty(item.getIdClientZalog()), dataStyle);
                ExcelStyles.setCellValue(dataRow, col++, nullToEmpty(item.getFioZalog()), dataStyle);

                // Existing field
                ExcelStyles.setCellValue(dataRow, col++, nullToEmpty(item.getPinflInnZalog()), dataStyle);

                ExcelStyles.setCellValue(dataRow, col++, nullToEmpty(item.getAddressNci()), dataStyle);
                ExcelStyles.setCellValue(dataRow, col++, nullToEmpty(item.getCertificateNum()), dataStyle);
                ExcelStyles.setCellValue(dataRow, col++, item.getCertificateDate() != null
                        ? item.getCertificateDate().toString() : "", dataStyle);

                ExcelStyles.setCellValue(dataRow, col++, item.getCadastreType(), dataStyle);
                ExcelStyles.setCellValue(dataRow, col++, nullToEmpty(item.getCadastreNum()), dataStyle);
                ExcelStyles.setCellValue(dataRow, col++, nullToEmpty(item.getReystrNum()), dataStyle);

                ExcelStyles.setCellValue(dataRow, col++, item.getSquare(), dataStyle);
                ExcelStyles.setCellValue(dataRow, col++, item.getOwnership(), dataStyle);

                // CADASTRE_AMOUNT (divide by 100)
                double cad_amount = item.getCadastreAmount() != null
                        ? new BigDecimal(item.getCadastreAmount())
                        .divide(new BigDecimal(100), 2, RoundingMode.UNNECESSARY)
                        .doubleValue()
                        : 0;
                Cell amountCell = dataRow.createCell(col++);
                amountCell.setCellValue(cad_amount);
                amountCell.setCellStyle(amountStyle);

                ExcelStyles.setCellValue(dataRow, col++, nullToEmpty(item.getEstimateInn()), dataStyle);
                ExcelStyles.setCellValue(dataRow, col++, nullToEmpty(item.getDocDate()), dataStyle);

                ExcelStyles.setCellValue(dataRow, col++, nullToEmpty(item.getGuarId()), dataStyle);
                ExcelStyles.setCellValue(dataRow, col++, nullToEmpty(item.getGuarAddress()), dataStyle);

                // GUAR_AMOUNT
                double guar_amount = item.getGuarAmount() != null
                        ? new BigDecimal(item.getGuarAmount())
                        .divide(new BigDecimal(100), 2, RoundingMode.UNNECESSARY)
                        .doubleValue()
                        : 0;
                Cell amountCell1 = dataRow.createCell(col++);
                amountCell1.setCellValue(guar_amount);
                amountCell1.setCellStyle(amountStyle);

                ExcelStyles.setCellValue(dataRow, col++, nullToEmpty(item.getGuarCurr()), dataStyle);

                // --- NEW FIELD STATUS ---
                ExcelStyles.setCellValue(dataRow, col++, nullToEmpty(item.getStatus()), dataStyle);
            }

            sheet.setColumnWidth(1, 2000);
            sheet.setColumnWidth(6, 8000);
            sheet.setColumnWidth(11, 13000);
            sheet.setColumnWidth(20, 2000);
            for (int i = 0; i < headers.length; i++) {
                if (i != 1 && i != 6 && i != 11 && i !=20) sheet.autoSizeColumn(i);
            }

            workbook.write(baos);
            workbook.dispose();
            return baos.toByteArray();
        }

}

    private String nullToEmpty(String str) {
        return str == null ? "" : str;

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

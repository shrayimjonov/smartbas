package uz.davrbank.zalogautoservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import uz.davrbank.zalogautoservice.aggregate.cloud.service.AutoMinioInterface;
import uz.davrbank.zalogautoservice.aggregate.dto.*;
import uz.davrbank.zalogautoservice.service.interfaces.AutoReportInterface;
import uz.davrbank.zalogautoservice.service.interfaces.AutoServiceInterface;
import uz.davrbank.zalogautoservice.utils.AutoExcelStyles;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class AutoReportService implements AutoReportInterface {

    private static final Logger logger = LoggerFactory.getLogger(AutoReportService.class);
    private final AutoServiceInterface autoService;
    private final AutoMinioInterface minioInterface;

    @Override
    public ResponseEntity<?> uploadReportToMinio(
            String startDate, String endDate, Integer state,
            String branch, Integer loanId, String clientId, String pinfl,
            String clientName, String stateNumber, String address, String userName) {

        logger.info("REQUEST: downloadReport - startDate: {}, endDate: {}, state={}, branch={}, loanId={}, clientId={}, pinfl={}, clientName={}, stateNumber={}, address={}",
                startDate, endDate, state, branch, loanId, clientId, pinfl, clientName, stateNumber, address);

        try {
            if (state == null) {
                return ResponseEntity.badRequest()
                        .body(new ErrorResponseDto(400, "The 'state' parameter is required."));
            }

            boolean startProvided = startDate != null && !startDate.isEmpty();
            boolean endProvided = endDate != null && !endDate.isEmpty();

            if (startProvided != endProvided) {
                return ResponseEntity.badRequest()
                        .body(new ErrorResponseDto(400, "Both 'startDate' and 'endDate' must be provided together."));
            }

            if (startProvided && startDate.equals(endDate)) {
                return ResponseEntity.badRequest()
                        .body(new ErrorResponseDto(400, "'startDate' and 'endDate' cannot be the same."));
            }

            AutoRequestDto requestParam = AutoRequestDto.builder()
                    .state(state)
                    .startDate(startProvided ? startDate : null)
                    .endDate(endProvided ? endDate : null)
                    .branch(branch)
                    .loanId(loanId)
                    .clientId(clientId)
                    .pinfl(pinfl)
                    .clientName(clientName)
                    .stateNumber(stateNumber)
                    .address(address)
                    .build();

            List<AutoDataDto> allData = getAllDataNonPaginated(requestParam);
            if (allData.isEmpty()) {
                String msg = startProvided
                        ? "No data found for the given date range."
                        : "No data found for this given parametr";

                return ResponseEntity.ok()
                        .body(new ErrorResponseDto(200, msg));
            }

            byte[] excelBytes = generateExcel(allData, userName);

            ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Asia/Tashkent"));
            String dateTimePart = now.format(DateTimeFormatter.ofPattern("dd.MM.yyyy_HH-mm"));

            String branchForMinio = (branch == null || branch.trim().isEmpty() || "all".equalsIgnoreCase(branch.trim()))
                    ? "all"
                    : branch.trim();

            String minioFileName = String.format("ZalogAuto_%s_%s.xlsx", dateTimePart, branchForMinio);

            String branchForUser = branchForMinio;
            String userFileName = "ZalogAuto_" + branchForUser + ".xlsx";

            String fileUrl = minioInterface.createExcelFileInMinio(
                    startDate, endDate, state, branch, loanId,
                    clientId, pinfl, clientName, stateNumber, address,
                    excelBytes, minioFileName);

            logger.info("Excel file uploaded to MinIO: {}", fileUrl);

            return buildExcelResponse(excelBytes, userFileName);

        } catch (Exception e) {
            logger.error("ERROR in uploadReport: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError()
                    .body(new ErrorResponseDto(500, e.getMessage() != null ? e.getMessage() : "Internal server error"));
        }
    }

    private List<AutoDataDto> getAllDataNonPaginated(AutoRequestDto requestParam) {
        logger.info("Fetching full report data (non-paginated) with state={}", requestParam.getState());
        AutoResponseDto response = autoService.getAutoZalogReport(requestParam);
        if (response == null || response.getData() == null) {
            logger.warn("Report returned null or empty data");
            return List.of();
        }
        logger.info("Successfully fetched {} records", response.getData().size());
        return response.getData();
    }

    private byte[] generateExcel(List<AutoDataDto> data, String userName) throws Exception {
        try (SXSSFWorkbook workbook = new SXSSFWorkbook(1000);
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            workbook.setCompressTempFiles(true);
            SXSSFSheet sheet = workbook.createSheet("AutoZalog Report");

            CellStyle headerStyle = AutoExcelStyles.createHeaderStyle(workbook);
            CellStyle dataStyle = AutoExcelStyles.createDataStyle(workbook);
            CellStyle amountStyle = AutoExcelStyles.createAmountStyle(workbook);
            CellStyle titleStyle = AutoExcelStyles.createBoldStyle(workbook);

            int rowIndex = 1;

            Row row = sheet.createRow(rowIndex++);
            row.createCell(0).setCellValue("Отчет:");
            row.createCell(1).setCellValue("Отчёт о залогах по автокредитам");
            row.getCell(0).setCellStyle(titleStyle);
            row.getCell(1).setCellStyle(dataStyle);

            row = sheet.createRow(rowIndex++);
            row.createCell(0).setCellValue("Отделение:");
            row.createCell(1).setCellValue("ЧАКБ «Davr bank» — Департамент ИТ, Отдел интеграции");
            row.getCell(0).setCellStyle(titleStyle);
            row.getCell(1).setCellStyle(dataStyle);

            row = sheet.createRow(rowIndex++);
            row.createCell(0).setCellValue("Загружено:");
            row.createCell(1).setCellValue(userName != null ? userName : "system");
            row.getCell(0).setCellStyle(titleStyle);
            row.getCell(1).setCellStyle(dataStyle);

            String now = ZonedDateTime.now(ZoneId.of("Asia/Tashkent"))
                    .format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"));
            row = sheet.createRow(rowIndex++);
            row.createCell(0).setCellValue("Дата:");
            row.createCell(1).setCellValue(now);
            row.getCell(0).setCellStyle(titleStyle);
            row.getCell(1).setCellStyle(dataStyle);

            rowIndex++;

            String[] headers = {
                    "ИД Обеспечения", "Филиал", "ИД анкеты", "Cсудный счет", "ИД клиента (Заемщик)", "ФИО (Заемщик)", "ПИНФЛ, ИНН (Заемщик)",
                    "ИД клиента (Залогодатель)", "ФИО (Залогодатель)", "ПИНФЛ, ИНН (Залогодатель)", "Адрес NCI клиенты", "Тип авто", "Год выпуска",
                    "Марка", "Модель", "Номер двигателя", "Номер кузова", "Гос. номер",
                    "Тех. паспорт", "Дата документа", "Номер шасси", "Тип транспорта", "Тип обеспечения", "Адрес обеспечения", "Сумма залога", "Код валюты", "Статус анкеты"
            };

            Row headerRow = sheet.createRow(rowIndex++);
            headerRow.setHeightInPoints(35);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            for (AutoDataDto item : data) {
                Row dataRow = sheet.createRow(rowIndex++);
                int col = 0;

                AutoExcelStyles.setCellValue(dataRow, col++, nullToEmpty(String.valueOf(item.getPk())), dataStyle);
                AutoExcelStyles.setCellValue(dataRow, col++, nullToEmpty(item.getBranch()), dataStyle);
                AutoExcelStyles.setCellValue(dataRow, col++, item.getIdAnketa() != null ? item.getIdAnketa() : 0, dataStyle);
                AutoExcelStyles.setCellValue(dataRow, col++, nullToEmpty(item.getAccount()), dataStyle);
                AutoExcelStyles.setCellValue(dataRow, col++, nullToEmpty(item.getIdClientZaem()), dataStyle);
                AutoExcelStyles.setCellValue(dataRow, col++, nullToEmpty(item.getFioZaem()), dataStyle);
                AutoExcelStyles.setCellValue(dataRow, col++, nullToEmpty(item.getPinflInnZaem()), dataStyle);
                AutoExcelStyles.setCellValue(dataRow, col++, nullToEmpty(item.getIdClientZalog()), dataStyle);
                AutoExcelStyles.setCellValue(dataRow, col++, nullToEmpty(item.getNameZalog()), dataStyle);
                AutoExcelStyles.setCellValue(dataRow, col++, nullToEmpty(item.getPinflInnZalog()), dataStyle);
                AutoExcelStyles.setCellValue(dataRow, col++, nullToEmpty(item.getAddressNci()), dataStyle);
                AutoExcelStyles.setCellValue(dataRow, col++, nullToEmpty(item.getCarType()), dataStyle);
                AutoExcelStyles.setCellValue(dataRow, col++, item.getCarDateMade() != null ? item.getCarDateMade() : 0, dataStyle);
                AutoExcelStyles.setCellValue(dataRow, col++, nullToEmpty(item.getCarMarka()), dataStyle);
                AutoExcelStyles.setCellValue(dataRow, col++, nullToEmpty(item.getCarModel()), dataStyle);
                AutoExcelStyles.setCellValue(dataRow, col++, nullToEmpty(item.getEngineNum()), dataStyle);
                AutoExcelStyles.setCellValue(dataRow, col++, nullToEmpty(item.getBodyNum()), dataStyle);
                AutoExcelStyles.setCellValue(dataRow, col++, nullToEmpty(item.getStateNumber()), dataStyle);
                AutoExcelStyles.setCellValue(dataRow, col++, nullToEmpty(item.getTexPassport()), dataStyle);
                AutoExcelStyles.setCellValue(dataRow, col++, item.getDocDate() != null ? item.getDocDate() : "", dataStyle);
                AutoExcelStyles.setCellValue(dataRow, col++, nullToEmpty(item.getChassisNumber()), dataStyle);
                AutoExcelStyles.setCellValue(dataRow, col++, nullToEmpty(item.getTransportType()), dataStyle);
                AutoExcelStyles.setCellValue(dataRow, col++, nullToEmpty(String.valueOf(item.getGuarId())), dataStyle);
                AutoExcelStyles.setCellValue(dataRow, col++, nullToEmpty(item.getGuarAddress()), dataStyle);

                double guarAmount = item.getGuarAmount() != null
                        ? BigDecimal.valueOf(item.getGuarAmount())
                        .divide(new BigDecimal(100), 2, RoundingMode.UNNECESSARY)
                        .doubleValue()
                        : 0.0;
                Cell amountCell = dataRow.createCell(col++);
                amountCell.setCellValue(guarAmount);
                amountCell.setCellStyle(amountStyle);

                AutoExcelStyles.setCellValue(dataRow, col++, nullToEmpty(item.getGuarCurr()), dataStyle);
                AutoExcelStyles.setCellValue(dataRow, col++, nullToEmpty(item.getStatus()), dataStyle);
            }

            sheet.setColumnWidth(0, 18 * 256);
            sheet.setColumnWidth(1, 15 * 256);
            sheet.setColumnWidth(2, 20 * 256);
            sheet.setColumnWidth(3, 25 * 256);
            sheet.setColumnWidth(4, 22 * 256);
            sheet.setColumnWidth(5, 40 * 256);
            sheet.setColumnWidth(6, 20 * 256);
            sheet.setColumnWidth(7, 22 * 256);
            sheet.setColumnWidth(8, 40 * 256);
            sheet.setColumnWidth(9, 20 * 256);
            sheet.setColumnWidth(10, 40 * 256);
            sheet.setColumnWidth(11, 20 * 256);
            sheet.setColumnWidth(12, 15 * 256);
            sheet.setColumnWidth(13, 20 * 256);
            sheet.setColumnWidth(14, 20 * 256);
            sheet.setColumnWidth(15, 25 * 256);
            sheet.setColumnWidth(16, 25 * 256);
            sheet.setColumnWidth(17, 20 * 256);
            sheet.setColumnWidth(18, 25 * 256);
            sheet.setColumnWidth(19, 20 * 256);
            sheet.setColumnWidth(20, 25 * 256);
            sheet.setColumnWidth(21, 20 * 256);
            sheet.setColumnWidth(22, 20 * 256);
            sheet.setColumnWidth(23, 40 * 256);
            sheet.setColumnWidth(24, 22 * 256);
            sheet.setColumnWidth(25, 15 * 256);
            sheet.setColumnWidth(26, 20 * 256);

            workbook.write(baos);
            workbook.dispose();
            return baos.toByteArray();
        }
    }

    private String nullToEmpty(String str) {
        return str == null ? "" : str;
    }

    private ResponseEntity<ByteArrayResource> buildExcelResponse(byte[] bytes, String fileName) {
        HttpHeaders headers = new HttpHeaders();
        String disposition = "attachment; filename*=%s".formatted(fileName);
        headers.add(HttpHeaders.CONTENT_DISPOSITION, disposition);
        headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
        headers.setCacheControl("no-cache");
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(bytes.length)
                .body(new ByteArrayResource(bytes));
    }
}
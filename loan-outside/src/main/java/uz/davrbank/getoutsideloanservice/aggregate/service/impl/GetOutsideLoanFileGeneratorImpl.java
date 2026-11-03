package uz.davrbank.getoutsideloanservice.aggregate.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.stereotype.Service;
import uz.davrbank.getoutsideloanservice.aggregate.dto.integration.response.GetOutsideLoanPageableResponse;
import uz.davrbank.getoutsideloanservice.aggregate.service.GetOutsideLoanFileGenerator;
import uz.davrbank.getoutsideloanservice.utils.ExcelStyles;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class GetOutsideLoanFileGeneratorImpl implements GetOutsideLoanFileGenerator {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
    private static final BigDecimal AMOUNT_DIVISOR = BigDecimal.valueOf(100);
    private static final int AMOUNT_SCALE = 2;
    private static final int RATE_SCALE = 7;
    private static final String[] SCALAR_HEADERS = {
            "Статус",
            "МФО",
            "№ Анкеты",
            "Балансовый счет основного долга",
            "Валюта",
            "Уникал",
            "Счет",
            "Метод начисления",
            "Наименование кредитора",
            "№ кредитного договора",
            "Входящий Остаток (сум)",
            "Оборот Дебет (сум)",
            "Оборот Кредит (сум)",
            "Исходящий Остаток (сум)",
            "Входящий Остаток (валюта)",
            "Оборот Дебет (валюта)",
            "Оборот Кредит (валюта)",
            "Исходящий Остаток (валюта)",
            "Счет процентов",
            "Дата начала",
            "Дата завершения",
            "Процентная ставка"
    };
    private static final String GROUP_HEADER_OSV_OSN_DOLG = "График освоения. Основной долг";
    private static final String GROUP_HEADER_PROC_ZADOL = "График погашения. Проценты по срочной задолженности";
    private static final String GROUP_HEADER_POG_OSN_DOLG = "График погашения. Основной долг";
    private static final String[] OSV_OSN_DOLG_CHILD_HEADERS = {
            "Дата выплаты/погашения",
            "Сумма выплаты/погашения",
            "Номер освоения",
            "Статус"
    };
    private static final String[] PROC_ZADOL_CHILD_HEADERS = {
            "Дата выплаты/погашения",
            "Сумма выплаты/погашения",
            "Номер погашения",
            "Статус"
    };
    private static final String[] POG_OSN_DOLG_CHILD_HEADERS = {
            "Дата выплаты/погашения",
            "Сумма выплаты/погашения",
            "Номер погашения",
            "Статус"
    };
    private static final int[] COLUMN_WIDTHS = {
            12, 13, 10, 18, 9, 9, 21, 40, 35, 22, 21, 21, 21, 21, 19, 17, 17, 19, 21, 11, 14, 15,
            18, 20, 16, 14,
            18, 20, 16, 14,
            18, 20, 16, 14
    };

    @Override
    public byte[] generate(List<GetOutsideLoanPageableResponse> data, String username, String reportPeriod) throws IOException {
        try (SXSSFWorkbook workbook = new SXSSFWorkbook(1000);
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            workbook.setCompressTempFiles(true);
            SXSSFSheet sheet = workbook.createSheet("Портфел");

            CellStyle headerStyle = ExcelStyles.createHeaderStyle(workbook);
            CellStyle dataStyle = ExcelStyles.createDataStyle(workbook);
            CellStyle amountStyle = ExcelStyles.createAmountStyle(workbook);
            CellStyle rateStyle = ExcelStyles.createRateStyle(workbook);

            CellStyle titleStyle = workbook.createCellStyle();
            Font titleFont = workbook.createFont();
            titleFont.setBold(true);
            titleStyle.setFont(titleFont);

            int rowIndex = 0;
            rowIndex = writeMetaInfo(sheet, rowIndex, username, reportPeriod, titleStyle, dataStyle);
            rowIndex++;
            rowIndex = writeHeader(sheet, rowIndex, headerStyle);

            for (GetOutsideLoanPageableResponse item : safeList(data)) {
                rowIndex = writeLoanBlock(sheet, rowIndex, item, dataStyle, amountStyle, rateStyle);
            }

            applyColumnWidths(sheet);

            workbook.write(outputStream);
            workbook.dispose();
            return outputStream.toByteArray();
        } catch (IOException e) {
            log.error("Failed to generate excel file: {}", e.getMessage(), e);
            throw e;
        }
    }

    private int writeMetaInfo(
            SXSSFSheet sheet,
            int rowIndex,
            String username,
            String reportPeriod,
            CellStyle titleStyle,
            CellStyle dataStyle
    ) {
        Row row = sheet.createRow(rowIndex++);
        ExcelStyles.setCellValue(row, 0, "Отчет:", titleStyle);
        ExcelStyles.setCellValue(row, 1, "Портфел полученных кредитов", dataStyle);

        row = sheet.createRow(rowIndex++);
        ExcelStyles.setCellValue(row, 0, "Загружено:", titleStyle);
        ExcelStyles.setCellValue(row, 1, username != null ? username : "anonymous", dataStyle);

        row = sheet.createRow(rowIndex++);
        ExcelStyles.setCellValue(row, 0, "Период отчета:", titleStyle);
        ExcelStyles.setCellValue(row, 1, nullToEmpty(reportPeriod), dataStyle);

        row = sheet.createRow(rowIndex++);
        ExcelStyles.setCellValue(row, 0, "Дата формирования:", titleStyle);
        ExcelStyles.setCellValue(
                row,
                1,
                ZonedDateTime.now(ZoneId.of("Asia/Tashkent")).format(DATE_TIME_FORMATTER),
                dataStyle
        );

        return rowIndex;
    }

    private int writeHeader(SXSSFSheet sheet, int rowIndex, CellStyle headerStyle) {
        Row topHeaderRow = sheet.createRow(rowIndex++);
        Row bottomHeaderRow = sheet.createRow(rowIndex++);
        topHeaderRow.setHeightInPoints(42);
        bottomHeaderRow.setHeightInPoints(42);

        int colIndex = 0;
        for (String header : SCALAR_HEADERS) {
            ExcelStyles.setCellValue(topHeaderRow, colIndex, header, headerStyle);
            ExcelStyles.setCellValue(bottomHeaderRow, colIndex, "", headerStyle);
            mergeCells(sheet, topHeaderRow.getRowNum(), bottomHeaderRow.getRowNum(), colIndex, colIndex);
            colIndex++;
        }

        colIndex = writeGroupedHeader(
                sheet,
                topHeaderRow,
                bottomHeaderRow,
                colIndex,
                GROUP_HEADER_OSV_OSN_DOLG,
                OSV_OSN_DOLG_CHILD_HEADERS,
                headerStyle
        );
        colIndex = writeGroupedHeader(
                sheet,
                topHeaderRow,
                bottomHeaderRow,
                colIndex,
                GROUP_HEADER_POG_OSN_DOLG,
                POG_OSN_DOLG_CHILD_HEADERS,
                headerStyle
        );
        writeGroupedHeader(
                sheet,
                topHeaderRow,
                bottomHeaderRow,
                colIndex,
                GROUP_HEADER_PROC_ZADOL,
                PROC_ZADOL_CHILD_HEADERS,
                headerStyle
        );


        return rowIndex;
    }

    private int writeGroupedHeader(
            SXSSFSheet sheet,
            Row topHeaderRow,
            Row bottomHeaderRow,
            int startColumn,
            String groupName,
            String[] childHeaders,
            CellStyle headerStyle
    ) {
        ExcelStyles.setCellValue(topHeaderRow, startColumn, groupName, headerStyle);
        for (int i = 1; i < childHeaders.length; i++) {
            ExcelStyles.setCellValue(topHeaderRow, startColumn + i, "", headerStyle);
        }
        for (int i = 0; i < childHeaders.length; i++) {
            ExcelStyles.setCellValue(bottomHeaderRow, startColumn + i, childHeaders[i], headerStyle);
        }
        mergeCells(
                sheet,
                topHeaderRow.getRowNum(),
                topHeaderRow.getRowNum(),
                startColumn,
                startColumn + childHeaders.length - 1
        );
        return startColumn + childHeaders.length;
    }

    private int writeLoanBlock(
            SXSSFSheet sheet,
            int rowIndex,
            GetOutsideLoanPageableResponse item,
            CellStyle dataStyle,
            CellStyle amountStyle,
            CellStyle rateStyle
    ) {
        GetOutsideLoanPageableResponse loanItem = item != null ? item : new GetOutsideLoanPageableResponse();
        List<GetOutsideLoanPageableResponse.LoanScheduleItemResponse> osvOsnDolg = safeList(loanItem.getOsvOsnDolg());
        List<GetOutsideLoanPageableResponse.LoanScheduleItemResponse> procZadol = safeList(loanItem.getProcZadol());
        List<GetOutsideLoanPageableResponse.LoanScheduleItemResponse> pogOsnDolg = safeList(loanItem.getPogOsnDolg());
        int blockRowCount = resolveBlockRowCount(osvOsnDolg, procZadol, pogOsnDolg);

        for (int rowOffset = 0; rowOffset < blockRowCount; rowOffset++) {
            Row row = sheet.createRow(rowIndex++);
            writeScalarCells(row, loanItem, dataStyle, amountStyle, rateStyle, rowOffset == 0);

            int colIndex = SCALAR_HEADERS.length;
            colIndex = writeGroupRowValues(
                    row,
                    colIndex,
                    getItemByIndex(osvOsnDolg, rowOffset),
                    dataStyle,
                    amountStyle
            );
            colIndex = writeGroupRowValues(
                    row,
                    colIndex,
                    getItemByIndex(pogOsnDolg, rowOffset),
                    dataStyle,
                    amountStyle
            );
            writeGroupRowValues(
                    row,
                    colIndex,
                    getItemByIndex(procZadol, rowOffset),
                    dataStyle,
                    amountStyle
            );
        }

        return rowIndex;
    }

    private void writeScalarCells(
            Row row,
            GetOutsideLoanPageableResponse item,
            CellStyle dataStyle,
            CellStyle amountStyle,
            CellStyle rateStyle,
            boolean firstRowInBlock
    ) {
        int colIndex = 0;
        ExcelStyles.setCellValue(row, colIndex++, nullToEmpty(item.getStatus()), dataStyle);
        ExcelStyles.setCellValue(row, colIndex++, nullToEmpty(item.getBranch()), dataStyle);
        ExcelStyles.setCellValue(row, colIndex++, item.getId(), dataStyle);
        ExcelStyles.setCellValue(row, colIndex++, nullToEmpty(item.getAccBal()), dataStyle);
        ExcelStyles.setCellValue(row, colIndex++, nullToEmpty(item.getCurrency()), dataStyle);
        ExcelStyles.setCellValue(row, colIndex++, nullToEmpty(item.getClient()), dataStyle);
        ExcelStyles.setCellValue(row, colIndex++, nullToEmpty(item.getAccount()), dataStyle);
        ExcelStyles.setCellValue(row, colIndex++, nullToEmpty(item.getMethodNach()), dataStyle);
        ExcelStyles.setCellValue(row, colIndex++, nullToEmpty(item.getName()), dataStyle);
        ExcelStyles.setCellValue(row, colIndex++, nullToEmpty(item.getNumDog()), dataStyle);
        ExcelStyles.setCellValue(row, colIndex++, toDuplicatedAmountCellValue(item.getSInSum(), firstRowInBlock), amountStyle);
        ExcelStyles.setCellValue(row, colIndex++, toDuplicatedAmountCellValue(item.getDtSum(), firstRowInBlock), amountStyle);
        ExcelStyles.setCellValue(row, colIndex++, toDuplicatedAmountCellValue(item.getCtSum(), firstRowInBlock), amountStyle);
        ExcelStyles.setCellValue(row, colIndex++, toDuplicatedAmountCellValue(item.getSOutSum(), firstRowInBlock), amountStyle);
        ExcelStyles.setCellValue(row, colIndex++, toDuplicatedAmountCellValue(item.getSIn(), firstRowInBlock), amountStyle);
        ExcelStyles.setCellValue(row, colIndex++, toDuplicatedAmountCellValue(item.getDt(), firstRowInBlock), amountStyle);
        ExcelStyles.setCellValue(row, colIndex++, toDuplicatedAmountCellValue(item.getCt(), firstRowInBlock), amountStyle);
        ExcelStyles.setCellValue(row, colIndex++, toDuplicatedAmountCellValue(item.getSOut(), firstRowInBlock), amountStyle);
        ExcelStyles.setCellValue(row, colIndex++, nullToEmpty(item.getAccPros()), dataStyle);
        ExcelStyles.setCellValue(row, colIndex++, nullToEmpty(item.getDateBeg()), dataStyle);
        ExcelStyles.setCellValue(row, colIndex++, nullToEmpty(item.getDateEnd()), dataStyle);
        ExcelStyles.setCellValue(row, colIndex++, toNumericCellValue(item.getRate()), rateStyle);
    }

    private int writeGroupRowValues(
            Row row,
            int startColumn,
            GetOutsideLoanPageableResponse.LoanScheduleItemResponse item,
            CellStyle dataStyle,
            CellStyle amountStyle
    ) {
        if (item == null) {
            return writeEmptyCells(row, startColumn, dataStyle, amountStyle, dataStyle, dataStyle);
        }

        int colIndex = startColumn;
        ExcelStyles.setCellValue(row, colIndex++, nullToEmpty(item.getDatePog()), dataStyle);
        ExcelStyles.setCellValue(row, colIndex++, toAmountCellValue(item.getSummaPog()), amountStyle);
        ExcelStyles.setCellValue(row, colIndex++, nullToEmpty(item.getNumPog()), dataStyle);
        Integer status = item.getStatusPog();
        if (status == null) {
            ExcelStyles.setCellValue(row, colIndex++, "Неоплачено", dataStyle);
        } else {
            if (status == 0) {
                ExcelStyles.setCellValue(row, colIndex++, "Оплачено", dataStyle);
            } else {
                ExcelStyles.setCellValue(row, colIndex++, "Неоплачено", dataStyle);
            }
        }
        return colIndex;
    }

    private int writeEmptyCells(Row row, int startColumn, CellStyle... styles) {
        int colIndex = startColumn;
        for (CellStyle style : styles) {
            ExcelStyles.setCellValue(row, colIndex++, "", style);
        }
        return colIndex;
    }

    private int resolveBlockRowCount(
            List<GetOutsideLoanPageableResponse.LoanScheduleItemResponse> osvOsnDolg,
            List<GetOutsideLoanPageableResponse.LoanScheduleItemResponse> procZadol,
            List<GetOutsideLoanPageableResponse.LoanScheduleItemResponse> pogOsnDolg
    ) {
        return Math.max(1, Math.max(osvOsnDolg.size(), Math.max(procZadol.size(), pogOsnDolg.size())));
    }

    private void mergeCells(SXSSFSheet sheet, int firstRow, int lastRow, int firstColumn, int lastColumn) {
        if (firstRow == lastRow && firstColumn == lastColumn) {
            return;
        }
        sheet.addMergedRegionUnsafe(new CellRangeAddress(firstRow, lastRow, firstColumn, lastColumn));
    }

    private void applyColumnWidths(SXSSFSheet sheet) {
        for (int i = 0; i < COLUMN_WIDTHS.length; i++) {
            sheet.setColumnWidth(i, COLUMN_WIDTHS[i] * 256);
        }
    }

    private <T> List<T> safeList(List<T> value) {
        return value == null ? Collections.emptyList() : value;
    }

    private <T> T getItemByIndex(List<T> items, int index) {
        return index >= 0 && index < items.size() ? items.get(index) : null;
    }

    private String nullToEmpty(String value) {
        return value == null ? "" : value;
    }

    private Object toAmountCellValue(BigDecimal value) {
        if (value == null) {
            return "-";
        }

        BigDecimal normalizedValue = value.divide(AMOUNT_DIVISOR, AMOUNT_SCALE, RoundingMode.HALF_UP);
        return normalizedValue.doubleValue();
    }

    private Object toDuplicatedAmountCellValue(BigDecimal value, boolean firstRowInBlock) {
        return firstRowInBlock ? toAmountCellValue(value) : 0D;
    }

    private Object toNumericCellValue(BigDecimal value) {
        if (value == null) {
            return "-";
        }

        BigDecimal normalizedValue = value.setScale(GetOutsideLoanFileGeneratorImpl.RATE_SCALE, RoundingMode.HALF_UP);
        return normalizedValue.doubleValue();
    }
}

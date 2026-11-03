package uz.davrbank.korinapogashenyaservice.aggregate.service.impl;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.stereotype.Service;
import uz.davrbank.korinapogashenyaservice.aggregate.dto.integration.response.PogasheniyaGetPageableResponse;
import uz.davrbank.korinapogashenyaservice.aggregate.service.PogasheniyaFileGenerator;
import uz.davrbank.korinapogashenyaservice.utils.ExcelStyles;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class PogasheniyaFileGeneratorImpl implements PogasheniyaFileGenerator {

    private static final BigDecimal HUNDRED = new BigDecimal(100);
    private static final DateTimeFormatter DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
    private static final String[] HEADERS = {
            "МФО банка + № анкеты", "№ анкеты", "Статус", "Ссудный счет", "Код валюты", "МФО банка", "Полное наименование клиента",
            "summa_1_7", "summa_8_30", "summa_31_90", "summa_91_180",
            "summa_181_365", "summa_366_730", "summa_731_1095", "summa_1096_1825",
            "summa_1826_3650", "сумма_1_3651"
    };

    @Override
    public byte[] generate(List<PogasheniyaGetPageableResponse> data, String username, String reportDate) throws IOException {
        try (SXSSFWorkbook workbook = new SXSSFWorkbook(1000);
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            workbook.setCompressTempFiles(true);
            SXSSFSheet sheet = workbook.createSheet();
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
            row.createCell(1).setCellValue("Корзина графика погашения");
            row.getCell(0).setCellStyle(titleStyle);
            row.getCell(1).setCellStyle(dataStyle);

            row = sheet.createRow(rowIndex++);
            row.createCell(0).setCellValue("Отделение:");
            row.createCell(1).setCellValue("ЧАКБ «Davr bank» — Департамент ИТ, Отдел интеграции");
            row.getCell(0).setCellStyle(titleStyle);
            row.getCell(1).setCellStyle(dataStyle);

            row = sheet.createRow(rowIndex++);
            row.createCell(0).setCellValue("Загружено:");
            row.createCell(1).setCellValue(username != null ? username : "anonymous");
            row.getCell(0).setCellStyle(titleStyle);
            row.getCell(1).setCellStyle(dataStyle);


            row = sheet.createRow(rowIndex++);
            row.createCell(0).setCellValue("Дата отчёта");
            row.createCell(1).setCellValue(reportDate);
            row.getCell(0).setCellStyle(titleStyle);
            row.getCell(1).setCellStyle(dataStyle);

            String formattedDateTime = ZonedDateTime.now(ZoneId.of("Asia/Tashkent"))
                    .format(DATE_TIME_FORMATTER);
            row = sheet.createRow(rowIndex++);
            row.createCell(0).setCellValue("Дата формирования");
            row.createCell(1).setCellValue(formattedDateTime);
            row.getCell(0).setCellStyle(titleStyle);
            row.getCell(1).setCellStyle(dataStyle);

            rowIndex++;

            Row headerRow = sheet.createRow(rowIndex++);
            headerRow.setHeightInPoints(30);
            for (int i = 0; i < HEADERS.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(HEADERS[i]);
                cell.setCellStyle(headerStyle);
            }

            for (PogasheniyaGetPageableResponse item : data) {
                Row dataRow = sheet.createRow(rowIndex++);
                int col = 0;

                ExcelStyles.setCellValue(dataRow, col++, (nullToEmpty(item.getBranch()) + item.getLdId()), dataStyle);
                ExcelStyles.setCellValue(dataRow, col++, item.getLdId(), dataStyle);
                ExcelStyles.setCellValue(dataRow, col++, nullToEmpty(item.getState()), dataStyle);
                ExcelStyles.setCellValue(dataRow, col++, nullToEmpty(item.getAccount()), dataStyle);
                ExcelStyles.setCellValue(dataRow, col++, nullToEmpty(item.getCodVal()), dataStyle);
                ExcelStyles.setCellValue(dataRow, col++, nullToEmpty(item.getBranch()), dataStyle);
                ExcelStyles.setCellValue(dataRow, col++, nullToEmpty(item.getClientName()), dataStyle);

                ExcelStyles.setCellValue(dataRow, col++, toAmount(item.getSumma17()).doubleValue(), amountStyle);
                ExcelStyles.setCellValue(dataRow, col++, toAmount(item.getSumma830()).doubleValue(), amountStyle);
                ExcelStyles.setCellValue(dataRow, col++, toAmount(item.getSumma3190()).doubleValue(), amountStyle);
                ExcelStyles.setCellValue(dataRow, col++, toAmount(item.getSumma91180()).doubleValue(), amountStyle);
                ExcelStyles.setCellValue(dataRow, col++, toAmount(item.getSumma181365()).doubleValue(), amountStyle);
                ExcelStyles.setCellValue(dataRow, col++, toAmount(item.getSumma366730()).doubleValue(), amountStyle);
                ExcelStyles.setCellValue(dataRow, col++, toAmount(item.getSumma7311095()).doubleValue(), amountStyle);
                ExcelStyles.setCellValue(dataRow, col++, toAmount(item.getSumma10961825()).doubleValue(), amountStyle);
                ExcelStyles.setCellValue(dataRow, col++, toAmount(item.getSumma18263650()).doubleValue(), amountStyle);
                ExcelStyles.setCellValue(dataRow, col, toAmount(item.getSumma13651()).doubleValue(), amountStyle);
            }

            sheet.setColumnWidth(2, 20 * 256);
            sheet.setColumnWidth(5, 40 * 256);
            for (int i = 0; i < HEADERS.length; i++) {
                if (i == 2 || i == 5) {
                    continue;
                }
                sheet.autoSizeColumn(i);
            }

            workbook.write(baos);
            workbook.dispose();
            return baos.toByteArray();
        }
    }

    private String nullToEmpty(String value) {
        return value == null ? "" : value;
    }

    private BigDecimal toAmount(Long value) {
        if (value == null) {
            return BigDecimal.ZERO;
        }
        return BigDecimal.valueOf(value).divide(HUNDRED, 2, RoundingMode.HALF_UP);
    }
}

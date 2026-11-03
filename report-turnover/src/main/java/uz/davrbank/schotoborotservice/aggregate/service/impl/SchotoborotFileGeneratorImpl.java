package uz.davrbank.schotoborotservice.aggregate.service.impl;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.stereotype.Service;
import uz.davrbank.schotoborotservice.aggregate.dto.integration.response.SchotoborotGetPageableResponse;
import uz.davrbank.schotoborotservice.aggregate.service.SchotoborotFileGenerator;
import uz.davrbank.schotoborotservice.utils.ExcelStyles;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class SchotoborotFileGeneratorImpl implements SchotoborotFileGenerator {

    public byte[] generate(List<SchotoborotGetPageableResponse> data, String username) throws IOException {
        try (SXSSFWorkbook workbook = new SXSSFWorkbook(1000);
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            SXSSFSheet sheet = workbook.createSheet("Schotoborot Report");
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
            row.createCell(1).setCellValue("Отчёт о счотоборот");
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

            String formattedDateTime = ZonedDateTime.now(ZoneId.of("Asia/Tashkent"))
                    .format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"));
            row = sheet.createRow(rowIndex++);
            row.createCell(0).setCellValue("Дата:");
            row.createCell(1).setCellValue(formattedDateTime);
            row.getCell(0).setCellStyle(titleStyle);
            row.getCell(1).setCellStyle(dataStyle);

            rowIndex++;

            String[] headers = {
                    "Идентификатор транзакции", "Номер документа", "Дата проведения", "Код операции", "Время проведения(АБС)", "Дата обработки(Терминал)", "Банк отправителя", "Счёт отправителя", "Наименование отправителя",
                    "Банк получателя", "Счёт получателя", "Наименование получателя", "Назначение платежа", "Сумма (дебет)","Сумма (кредит)", "Статус операции"
            };

            Row headerRow = sheet.createRow(rowIndex++);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
                headerRow.setHeightInPoints(30);
            }
            sheet.setColumnWidth(10, 20 * 256);

            for (SchotoborotGetPageableResponse item : data) {
                Row dataRow = sheet.createRow(rowIndex++);
                int col = 0;

                ExcelStyles.setCellValue(dataRow, col++, nullValidator(item.getTransactionId()), dataStyle);
                ExcelStyles.setCellValue(dataRow, col++, nullValidator(item.getDocNum()), dataStyle);
                ExcelStyles.setCellValue(dataRow, col++, nullValidator(item.getDocDate()), dataStyle);
                ExcelStyles.setCellValue(dataRow, col++, nullValidator(item.getCodOper()), dataStyle);
                ExcelStyles.setCellValue(dataRow, col++, nullValidator(item.getDataProv()), dataStyle);
                ExcelStyles.setCellValue(dataRow, col++, nullValidator(item.getTerminalDateOper()), dataStyle);

                ExcelStyles.setCellValue(dataRow, col++, nullValidator(item.getBankCl()), dataStyle);
                ExcelStyles.setCellValue(dataRow, col++, nullValidator(item.getAccCl()), dataStyle);
                ExcelStyles.setCellValue(dataRow, col++, nullValidator(item.getNameCl()), dataStyle);

                ExcelStyles.setCellValue(dataRow, col++, nullValidator(item.getBankCo()), dataStyle);
                ExcelStyles.setCellValue(dataRow, col++, nullValidator(item.getAccCo()), dataStyle);
                ExcelStyles.setCellValue(dataRow, col++, nullValidator(item.getNameCo()), dataStyle);
                ExcelStyles.setCellValue(dataRow, col++, nullValidator(item.getPurpose()), dataStyle);

                BigDecimal summaDebit = item.getSummaDebit() != null
                        ? item.getSummaDebit().divide(new BigDecimal(100), 2, RoundingMode.UNNECESSARY)
                        : BigDecimal.ZERO;
                ExcelStyles.setCellValue(dataRow, col++, summaDebit, amountStyle);

                BigDecimal summaCredit = item.getSummaCredit() != null
                        ? item.getSummaCredit().divide(new BigDecimal(100), 2, RoundingMode.UNNECESSARY)
                        : BigDecimal.ZERO;
                ExcelStyles.setCellValue(dataRow, col++, summaCredit, amountStyle);

                ExcelStyles.setCellValue(dataRow, col, nullValidator(item.getStateName()), dataStyle);
            }
            sheet.setColumnWidth(1, 5000);
            for (int i = 0; i < headers.length; i++) {
                if (i != 1) sheet.autoSizeColumn(i);
            }

            workbook.write(baos);
            workbook.dispose();
            return baos.toByteArray();
        }

    }

    private String nullValidator(String str) {
        return str == null ? "" : str;
    }
}

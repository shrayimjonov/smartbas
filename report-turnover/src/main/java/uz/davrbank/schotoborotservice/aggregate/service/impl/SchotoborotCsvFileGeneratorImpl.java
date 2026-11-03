package uz.davrbank.schotoborotservice.aggregate.service.impl;

import org.springframework.stereotype.Service;
import uz.davrbank.schotoborotservice.aggregate.dto.integration.response.SchotoborotGetPageableResponse;
import uz.davrbank.schotoborotservice.aggregate.service.SchotoborotCsvFileGenerator;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class SchotoborotCsvFileGeneratorImpl implements SchotoborotCsvFileGenerator {

    private static final String[] HEADERS = {
            "Идентификатор транзакции", "Номер документа", "Дата проведения", "Код операции", "Время проведения(АБС)", "Дата обработки(Терминал)", "Банк отправителя", "Счёт отправителя", "Наименование отправителя",
            "Банк получателя", "Счёт получателя", "Наименование получателя", "Назначение платежа", "Сумма", "Статус операции"
    };
    private static final BigDecimal HUNDRED = new BigDecimal(100);

    @Override
    public void writeCsv(OutputStream outputStream, List<SchotoborotGetPageableResponse> data) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8))) {
            writeLine(writer, HEADERS);
            for (SchotoborotGetPageableResponse item : data) {
                BigDecimal summa = item.getSummaDebit() != null
                        ? item.getSummaDebit().divide(HUNDRED, 2, RoundingMode.UNNECESSARY)
                        : BigDecimal.ZERO;
                String[] row = {
                        safeString(item.getTransactionId()),
                        safeString(item.getDocNum()),
                        safeString(item.getDocDate()),
                        safeString(item.getCodOper()),
                        safeString(item.getDataProv()),
                        safeString(item.getTerminalDateOper()),
                        safeString(item.getBankCl()),
                        safeString(item.getAccCl()),
                        safeString(item.getNameCl()),
                        safeString(item.getBankCo()),
                        safeString(item.getAccCo()),
                        safeString(item.getNameCo()),
                        safeString(item.getPurpose()),
                        summa.toPlainString(),
                        safeString(item.getStateName())
                };
                writeLine(writer, row);
            }
        }
    }

    private void writeLine(BufferedWriter writer, String[] values) throws IOException {
        StringBuilder line = new StringBuilder();
        for (int i = 0; i < values.length; i++) {
            if (i > 0) {
                line.append(',');
            }
            line.append(escapeCsv(values[i]));
        }
        writer.write(line.toString());
        writer.newLine();
    }

    private String escapeCsv(String value) {
        if (value == null) {
            return "";
        }
        String escaped = value.replace("\"", "\"\"");
        boolean needsQuotes = escaped.contains(",") || escaped.contains("\"") || escaped.contains("\n") || escaped.contains("\r");
        if (needsQuotes) {
            return "\"" + escaped + "\"";
        }
        return escaped;
    }

    private String safeString(String value) {
        return value == null ? "" : value;
    }
}

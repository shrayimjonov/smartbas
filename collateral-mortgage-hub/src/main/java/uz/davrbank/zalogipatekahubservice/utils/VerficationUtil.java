package uz.davrbank.zalogipatekahubservice.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import uz.davrbank.zalogipatekahubservice.exception.condition.DateTimeException;
import uz.davrbank.zalogipatekahubservice.exception.condition.NotFormatSuitableException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.List;

@Component
public class VerficationUtil {

    private static final Logger LOG = LoggerFactory.getLogger(VerficationUtil.class);


    public static String checkingVerifyState(Integer state) {
        LOG.info("Checking verification state :{}", state);


        if (state == null) {
            throw new IllegalArgumentException("State value cannot be null");
        }

        return switch (state) {
            case 0 -> "Неопределен";
            case 1 -> "Введен";
            case 2 -> "Утверждена";
            case 3 -> "Закрыт";
            case 98 -> "Отправлен в Головной банк";
            case 99 -> " Заблокирован для отправки ";
            default -> throw new NotFormatSuitableException("нет данных это выбранный номер" + state);
        };
    }


    public static String checkingCadastrType(Integer cadastrType) {
        LOG.info("Checking checkingCadastrType  :{}", cadastrType);


        if (cadastrType == null) {
            throw new IllegalArgumentException("State value cannot be null");
        }

        return switch (cadastrType) {
            case 1 -> " Здания и сооружения";
            case 2 -> "Земля";
            default -> throw new NotFormatSuitableException("нет данных это выбранный номер  (толка 1 или 2)  " + cadastrType);
        };
    }


    public static String checkingVerifyDate(String dateStr) {
        LOG.info("Checking date format: {}", dateStr);

        if (dateStr == null || dateStr.trim().isEmpty()) {
            throw new NotFormatSuitableException("Date value cannot be null or empty");
        }

        List<DateTimeFormatter> formatters = List.of(
                DateTimeFormatter.ofPattern("dd.MM.yyyy"),
                DateTimeFormatter.ISO_LOCAL_DATE
        );

        for (DateTimeFormatter formatter : formatters) {
            try {
                LocalDate date = LocalDate.parse(dateStr, formatter.withResolverStyle(ResolverStyle.STRICT));
                return date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")); // normalize format
            } catch (DateTimeException ignored) { }
        }

        throw new NotFormatSuitableException(
                "Invalid date format. Expected 'dd.MM.yyyy' — received: " + dateStr
        );
    }
}
package uz.davrbank.accounttocardservice.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

@Data
@NoArgsConstructor
public class DateUtils implements Serializable {
    public static LocalDateTime getDateTimeNow() {
        return LocalDateTime.now(ZoneId.of("Asia/Tashkent"));
    }

    public static LocalDate getDateNow() {
        return LocalDate.now(ZoneId.of("Asia/Tashkent"));
    }


    public static Boolean isCardExpired(String dateExpire) {
        var sd = new SimpleDateFormat("MMyy");

        try {
            var date = sd.parse(dateExpire);

            var dd = date.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();

            var nowDate = LocalDate.now(ZoneId.systemDefault());

            return nowDate.isAfter(dd);

        } catch (ParseException e) {
            throw new DateTimeException(e.getMessage());
        }
    }

    public static LocalDateTime getTimeNow() {
        return LocalDateTime.now(ZoneId.of("Asia/Tashkent"));
    }

    public static String getddMMyyyy(Date date) {
        final var dateTime = LocalDate.ofInstant(date.toInstant(), ZoneId.of("Asia/Tashkent"));
        final var newFormat = DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale.ROOT);

        return dateTime.format(newFormat);
    }
}

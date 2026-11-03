package uz.davrbank.esudservice.utils;

import lombok.experimental.UtilityClass;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@UtilityClass
public class DateUtil {

    public static String getTime() {
        LocalDateTime now = LocalDateTime.now();
        return formatDateTime(now);
    }

    public static LocalDate dateToLocalDate(Date date) {
        if (date == null) {
            return null;
        }
        return new java.sql.Date(date.getTime()).toLocalDate();
    }

    public static LocalDateTime dateToLocalDateTime(Date date) {
        if (date == null) {
            return null;
        }
        return new java.sql.Timestamp(date.getTime()).toLocalDateTime();
    }

    public static String formatDate(LocalDate date) {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        return format.format(date);
    }

    public static String formatDateTime(LocalDateTime dateTime) {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
        return dateTime.format(format);
    }

    public static String formatDate(LocalDateTime dateTime) {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        return dateTime.format(format);
    }

    public static LocalDate millisecondsToLocalDate(final Long milliseconds) {
        if (milliseconds == null) {
            return null;
        }
        Instant instant = Instant.ofEpochMilli(milliseconds);
        return LocalDate.ofInstant(instant, ZoneId.systemDefault());
    }
}

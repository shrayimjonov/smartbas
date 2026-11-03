package humo.integration.card.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

    public static String stringToDate(String date, String format) {
        DateFormat dateformat = new SimpleDateFormat(format);
        try {
            Date newDate = dateformat.parse(date);
            SimpleDateFormat outputDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            return outputDateFormat.format(newDate);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static String stringToDateV2(String date, String format) {
        DateFormat dateformat = new SimpleDateFormat(format);
        try {
            Date newDate = dateformat.parse(date);
            SimpleDateFormat outputDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'23:59:59");
            return outputDateFormat.format(newDate);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}

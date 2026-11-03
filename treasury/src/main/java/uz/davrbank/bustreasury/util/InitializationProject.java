package uz.davrbank.bustreasury.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

@Component
public class InitializationProject implements CommandLineRunner {

    private static final Logger LOG = LoggerFactory.getLogger(InitializationProject.class);

    @Override
    public void run(String... args) throws Exception {

        final Date date = new Date();
        final String strDateFormat = "dd-MM-yyyy :> hh:mm:ss a";
        final DateFormat dateFormat = new SimpleDateFormat(strDateFormat, Locale.US);
        final TimeZone timeZone = TimeZone.getTimeZone("Asia/Tashkent");
        dateFormat.setTimeZone(timeZone);
        final String formattedDate = dateFormat.format(date);
        LOG.info("BUS TREASURY is started! PORT: 7001, DATE: {}", formattedDate);
    }
}

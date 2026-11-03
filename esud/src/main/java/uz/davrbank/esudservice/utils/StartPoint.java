package uz.davrbank.esudservice.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

@Slf4j
@Component
public class StartPoint implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        final Date date = new Date();
        final String strDateFormat = "dd-MM-yyyy :> hh:mm:ss a";
        final DateFormat dateFormat = new SimpleDateFormat(strDateFormat, Locale.US);
        final TimeZone timeZone = TimeZone.getTimeZone("Asia/Tashkent");
        dateFormat.setTimeZone(timeZone);
        final String formattedDate = dateFormat.format(date);
        log.info("Esud Service Start : {} ", formattedDate);
    }
    public static void loggingApplicationParams(ConfigurableEnvironment environment) {
        var host = getHostAddress();
        log.info("""


                        ____________________SMART BAS______________________________

                        ESUD  PROJECT STARTED SUCCESSFULLY! \uD83D\uDE00
                        authors: Integration team (Urunov Khamdamboy, Murtozayev Manguberdi.)
                        application: ..........: {}
                        active profiles: ......: {}
                        address: ..............: {}
                        _____________________________________________________""",
                environment.getProperty("spring.application.name"),
                environment.getActiveProfiles(),
                "http://" + host + ":" + environment.getProperty("server.port")

        );
    }

    private static String getHostAddress() {
        String hostAddress;

        try {
            hostAddress = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            hostAddress = "localhost";
            log.error("Can't determine host address");
        }
        return hostAddress;
    }
}

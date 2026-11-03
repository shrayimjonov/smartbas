package uz.davrbank.partnervalidationservice.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.stereotype.Component;

import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

@Component
@Slf4j
public class StartProject implements CommandLineRunner {

    @Override
    public void run(String... args) {
        final Date date = new Date();
        final String strDateFormat = "dd-MM-yyyy :> hh:mm:ss a";
        final DateFormat dateFormat = new SimpleDateFormat(strDateFormat, Locale.US);
        final TimeZone timeZone = TimeZone.getTimeZone("Asia/Tashkent");
        dateFormat.setTimeZone(timeZone);
        final String formattedDate = dateFormat.format(date);
        log.info("PartnerValidation Project started : {} ", formattedDate);
    }

    public static void loggingApplicationParams(ConfigurableEnvironment environment) {
        String host;
        String profile = Arrays.stream(environment.getActiveProfiles()).findFirst().orElse("local");
        String port = environment.getProperty("server.port", "8080");

        if ("local".equals(profile)) {
            host = "localhost:" + port;
        } else {
            try {
                host = java.net.InetAddress.getLocalHost().getHostAddress() + ":" + port;
            } catch (UnknownHostException e) {
                host = "localhost:" + port;
            }
        }
        log.info("""
                        
                        
                        ____________________ PartnerValidation project ______________________________
                        
                        PartnerValidation project STARTED SUCCESSFULLY!!! \uD83D\uDE00
                        authors: Shukhratjon Rayimjonov
                        application: ..........: {}
                        active profiles: ......: {}
                        address: ..............: {}
                        swagger url: ..............: {}
                        _____________________________________________________""",
                environment.getProperty("spring.application.name"),
                environment.getActiveProfiles(),
                "http://" + host,
                "http://" + host + "/swagger-ui/index.html"
        );
    }
}

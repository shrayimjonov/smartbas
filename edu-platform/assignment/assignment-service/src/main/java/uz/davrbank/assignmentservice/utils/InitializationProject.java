package uz.davrbank.assignmentservice.utils;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

@Service
@RequiredArgsConstructor
public class InitializationProject implements CommandLineRunner {
    private static final Logger LOG = LoggerFactory.getLogger(InitializationProject.class);
    private final Environment environment;

    @Override
    public void run(String... args) {
        final Date date = new Date();
        final String strDateFormat = "dd-MM-yyyy :> hh:mm:ss a";
        final DateFormat dateFormat = new SimpleDateFormat(strDateFormat, Locale.US);
        final TimeZone timeZone = TimeZone.getTimeZone("Asia/Tashkent");
        dateFormat.setTimeZone(timeZone);
        final String formattedDate = dateFormat.format(date);
        LOG.info("ASSIGNMENT SERVICE STARTED!  {}", formattedDate);
        LOG.info("Address: http://{}",getHostAddress() + ":" + environment.getProperty("server.port"));
    }

    private static String getHostAddress() {
        String hostAddress;

        try {
            hostAddress = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            hostAddress = "localhost";
            LOG.error("Can't determine host address");
        }
        return hostAddress;
    }
}
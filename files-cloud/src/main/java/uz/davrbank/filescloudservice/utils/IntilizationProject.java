package uz.davrbank.filescloudservice.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.stereotype.Component;

import java.net.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Locale;
import java.util.TimeZone;

@Component
public class IntilizationProject implements CommandLineRunner {
    private static final Logger LOG = LoggerFactory.getLogger(IntilizationProject.class);


    @Override
    public void run(String... args) {
        final Date date = new Date();
        final String strDateFormat = "dd-MM-yyyy :> hh:mm:ss a";
        final DateFormat dateFormat = new SimpleDateFormat(strDateFormat, Locale.US);
        final TimeZone timeZone = TimeZone.getTimeZone("Asia/Tashkent");
        dateFormat.setTimeZone(timeZone);
        final String formattedDate = dateFormat.format(date);
        LOG.info("BAS: Files CLOUD is started! PORT: 7300, DATE: {}", formattedDate);
    }

    public static void loggingApplicationParams(ConfigurableEnvironment environment) {
        var host = getHostAddress();
        LOG.info("""
                        
                        
                        ______________________BAS PROJECT________________________________
                        
                        The PROJECT Files Cloud STARTED SUCCESSFULLY !!!
                        Author: ...............: Integration Team
                        application: ..........: {}
                        active profiles: ......: {}
                        address: ..............: {}
                        port : ................: {}
                        ___________________________________________________________________""",
                environment.getProperty("spring.application.name"),
                environment.getActiveProfiles(),
                "https://" + host + ":" + environment.getProperty("server.port"),
                environment.getProperty("server.port")

        );
    }

    private static String getHostAddress() {
        String hostAddress = "127.0.0.1";

        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface iface = interfaces.nextElement();
                if (iface.isLoopback() || !iface.isUp()) continue;

                Enumeration<InetAddress> addresses = iface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress addr = addresses.nextElement();
                    if (addr instanceof Inet4Address && !addr.isLoopbackAddress()) {
                        return addr.getHostAddress();
                    }
                }
            }
        } catch (SocketException e) {
            LOG.error("Unable to determine host IP", e);
        }

        return hostAddress;
    }
}
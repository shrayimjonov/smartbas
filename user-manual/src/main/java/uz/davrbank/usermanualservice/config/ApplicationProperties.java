package uz.davrbank.usermanualservice.config;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Author: Shukhratjon Rayimjonov
 * Date: 24.11.2025
 * Time: 13:38
 * Project: usermanual-service
 */

@Getter
@Setter
@ConfigurationProperties(prefix = "external", ignoreUnknownFields = false)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ApplicationProperties {

    MinIoProperties minIo = new MinIoProperties();


    @FieldDefaults(level = AccessLevel.PRIVATE)
    @Getter
    @Setter
    public static class BaseProperty {
        String host;
        String port;
    }

    @FieldDefaults(level = AccessLevel.PRIVATE)
    @Getter
    @Setter
    public static class MinIoProperties extends BaseProperty {

        String accessKey;
        String secretKey;
        String bucket;
        String documentBaseUrl;
    }
}

package uz.davrbank.schotoborotservice.aggregate.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "external.minio")
public class MinIOProperty {
    private String url;
    private String port;
    private String accessKey;
    private String secretKey;
    private String bucket;
}
package uz.davrbank.yeisvoservice.aggregate;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "external.minio")
public class MinioProperty {

    private String url;
    private String accessKey;
    private String secretKey;
    private String bucket;
}

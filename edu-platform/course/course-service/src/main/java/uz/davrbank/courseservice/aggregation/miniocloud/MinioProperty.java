package uz.davrbank.courseservice.aggregation.miniocloud;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Configuration
@ConfigurationProperties(prefix = "external.minio")
public class MinioProperty {
    private String url;
    private String accessKey;
    private String secretKey;
    private String bucket;
}
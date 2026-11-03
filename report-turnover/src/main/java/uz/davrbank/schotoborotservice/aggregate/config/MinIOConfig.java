package uz.davrbank.schotoborotservice.aggregate.config;

import io.minio.MinioClient;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class MinIOConfig {
    private final MinIOProperty property;

    @Bean
    public MinioClient minioClient() {
        return
                MinioClient.builder()
                        .endpoint(property.getUrl())
                        .credentials(property.getAccessKey(), property.getSecretKey())
                        .region("us-east-1")
                        .build();

    }
}

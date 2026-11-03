package uz.davrbank.clientaccounts.aggregate.cloud.config;

import io.minio.MinioClient;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uz.davrbank.clientaccounts.aggregate.cloud.dto.MinioProperty;

@Configuration
@RequiredArgsConstructor
public class MinioConfig {

    private final MinioProperty property;

    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint(property.getUrl())
                .credentials(property.getAccessKey(), property.getSecretKey())
                .build();
    }
}
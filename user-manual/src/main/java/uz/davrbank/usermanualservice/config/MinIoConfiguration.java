package uz.davrbank.usermanualservice.config;

import io.minio.MinioClient;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Author: Shukhratjon Rayimjonov
 * Date: 24.11.2025
 * Time: 16.42
 * Project: usermanual-service
 */

@Configuration
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class MinIoConfiguration {

    ApplicationProperties applicationProperties;

    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint(applicationProperties.getMinIo().getHost()+":"+applicationProperties.getMinIo().getPort())
                .credentials(
                        applicationProperties.getMinIo().getAccessKey(),
                        applicationProperties.getMinIo().getSecretKey()
                ).build();
    }
}

package uz.davrbank.yeisvoservice.aggregate.config;

import uz.davrbank.yeisvoservice.aggregate.MinioProperty;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class MinioConfig {
    private final MinioProperty minioProperty;

    @Bean
    public MinioClient minioClient() {

        return MinioClient.builder()
                .endpoint(minioProperty.getUrl())
                .credentials(minioProperty.getAccessKey(), minioProperty.getSecretKey())
                .build();
//        try {
//
//            boolean found = client.bucketExists(BucketExistsArgs.builder()
//                    .bucket(minioProperty.getBucket())
//                    .build());
//
//            if (!found) {
//                client.makeBucket(
//                        MakeBucketArgs.builder()
//                                .bucket(minioProperty.getBucket())
//                                .build()
//                );
//                log.info("✅ MinIO bucket created: {}", minioProperty.getBucket());
//            } else {
//                log.info("✅ MinIO bucket already exists: {}", minioProperty.getBucket());
//            }
//
//            return client;
//
//        } catch (Exception e) {
//            log.error("❌ Error initializing MinIO: {}", e.getMessage(), e);
//            return client;
//        }
    }
}

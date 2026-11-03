package uz.davrbank.clientaccounts.aggregate.cloud.service;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uz.davrbank.clientaccounts.aggregate.cloud.dto.MinioProperty;

import java.io.ByteArrayInputStream;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class ClientAccountMinioService implements ClientAccountMinioInterface {

    private final MinioClient minioClient;
    private final MinioProperty property;

    @Override
    public String uploadExcelToMinio(byte[] bytes, String iDate, String iBranch, String iAccList) {
        if (bytes == null || bytes.length == 0) {
            throw new IllegalArgumentException("Excel bytes cannot be empty");
        }

        try {
            ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Asia/Tashkent"));
            String year = String.valueOf(now.getYear());
            String month = String.format("%02d", now.getMonthValue());
            String safeAccList = iAccList != null ? iAccList.replace(",", "_") : "all";

            String fileName = String.format("ClientAccounts_%s_%s_%s.xlsx", iDate, iBranch, safeAccList);
            String objectPath = String.format("reports/clientaccounts/%s/%s/%s", year, month, fileName);

            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(property.getBucket())
                            .object(objectPath)
                            .stream(new ByteArrayInputStream(bytes), bytes.length, -1)
                            .contentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                            .build()
            );

            String url = String.format("%s/%s/%s", property.getUrl(), property.getBucket(), objectPath);
            log.info("Excel uploaded to MinIO: {}", url);
            return url;

        } catch (Exception e) {
            log.error("Failed to upload Excel to MinIO", e);
            throw new RuntimeException("MinIO upload failed", e);
        }
    }
}
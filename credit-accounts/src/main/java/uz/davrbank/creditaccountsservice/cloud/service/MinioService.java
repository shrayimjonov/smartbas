package uz.davrbank.creditaccountsservice.cloud.service;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uz.davrbank.creditaccountsservice.aggregate.request.CreditAccountRequestDTO;
import uz.davrbank.creditaccountsservice.cloud.dto.MinioProperty;
import uz.davrbank.creditaccountsservice.cloud.interfaces.MinioInterface;

import java.io.ByteArrayInputStream;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Service
@Slf4j
@RequiredArgsConstructor

public class MinioService implements MinioInterface {

    private final MinioClient minioClient;
    private final MinioProperty minioProperty;


    @Override
    public String createExcelFileInMinio(CreditAccountRequestDTO creditAccountRequestDTO, byte[] bytes) {

        if (bytes == null) {
            throw new IllegalArgumentException("Data (bytes) cannot be null");
        }

        log.info("Create Excel file in Minio: date: {}, branch: {}, state: {}, valType: {}, account: {}",
              creditAccountRequestDTO.getDate(), creditAccountRequestDTO.getBranch(), creditAccountRequestDTO.getState(),
                creditAccountRequestDTO.getValType(), creditAccountRequestDTO.getAccount());

        try {
            ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Asia/Tashkent"));

            String fileName;
            if ("00000".equals(creditAccountRequestDTO.getBranch())) {
                fileName = "Credit_accounts_(all_branches).xlsx";
            } else {
                fileName = String.format("Credit_accounts_(%s).xlsx", creditAccountRequestDTO.getBranch());
            }

            String year = String.valueOf(now.getYear());
            String month = String.format("%02d", now.getMonthValue());

            String objectPath = String.format("reports/credit/accounts/%s/%s/%s",
                    year, month, fileName);

            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(minioProperty.getBucket())
                            .object(objectPath)
                            .stream(new ByteArrayInputStream(bytes), bytes.length, -1)
                            .contentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                            .build()
            );

            log.info("✅ Excel file uploaded to MinIO: {}", objectPath);
            return objectPath;

        } catch (Exception e) {
            log.error("❌ Failed to upload Excel to MinIO: {}", e.getMessage(), e);
            throw new RuntimeException("Error uploading Excel to MinIO", e);
        }
    }

}

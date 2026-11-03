package uz.davrbank.zalogipatekaservice.cloud.service;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uz.davrbank.zalogipatekaservice.cloud.dto.MinioProperty;
import uz.davrbank.zalogipatekaservice.cloud.interfaces.MinioInterface;

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
    public String createExcelFileInMinio(String startDate, String endDate,
                                         Integer cadastreType, Integer state,
                                         String branch, Integer loanId,
                                         String clientId, String pinfl,
                                         String clientName, String cadastreNum,
                                         byte[] bytes) {
        if (bytes == null) {
            throw new IllegalArgumentException("Data (bytes) can not be null");
        }

        log.info("Saving excel to MinIO, startDate: {}, endDate: {}, cadastreType: {}, state: {}, branch: {}, loanId: {}, clientId: {}, pinfl: {}, clientName: {}, cadastreNum: {}",
                startDate, endDate, cadastreType, state, branch, loanId, clientId, pinfl, clientName, cadastreNum);

        try {
            ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Asia/Tashkent"));
            String fileName;
            if ((startDate == null || startDate.isBlank()) && (endDate == null || endDate.isBlank())) {
                fileName = String.format("ZalogIpoteka_all.xlsx");
            } else {
                fileName = String.format(
                        "ZalogIpoteka_Report_%s_to_%s.xlsx",
                        startDate, endDate
                );
            }
            String year = String.valueOf(now.getYear());
            String month = String.format("%02d", now.getMonthValue());
            String objectPath = String.format("reports/zalog/ipoteka/%s/%s/%s", year, month, fileName);

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



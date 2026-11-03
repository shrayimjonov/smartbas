package uz.davrbank.zalogautoservice.aggregate.cloud.service;

import io.minio.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uz.davrbank.zalogautoservice.aggregate.cloud.dto.MinioProperty;

import java.io.ByteArrayInputStream;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Service
@Slf4j
@RequiredArgsConstructor
public class AutoMinioService implements AutoMinioInterface {

    private final MinioClient minioClient;
    private final MinioProperty property;

    private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("dd.MM.yyyy_HH-mm");

    @Override
    public String createExcelFileInMinio(
            String startDate, String endDate,
            Integer state,
            String branch, Integer loanId,
            String clientId, String pinfl,
            String clientName, String stateNumber, String address,
            byte[] bytes) {

        return createExcelFileInMinio(startDate, endDate, state, branch, loanId,
                clientId, pinfl, clientName, stateNumber, address, bytes, null);
    }

    public String createExcelFileInMinio(
            String startDate, String endDate,
            Integer state,
            String branch, Integer loanId,
            String clientId, String pinfl,
            String clientName, String stateNumber, String address,
            byte[] bytes, String customFileName) {

        if (bytes == null || bytes.length == 0) {
            throw new IllegalArgumentException("Excel bytes cannot be null or empty");
        }

        try {
            ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Asia/Tashkent"));

            String branchPart = (branch == null || branch.trim().isEmpty() || "all".equalsIgnoreCase(branch.trim()))
                    ? "all"
                    : branch.trim();

            String dateTimePart = now.format(DTF);

            String fileNameForMinio = customFileName != null && !customFileName.isBlank()
                    ? customFileName
                    : String.format("ZalogAuto_%s_%s.xlsx", dateTimePart, branchPart);

            String year = String.valueOf(now.getYear());
            String month = String.format("%02d", now.getMonthValue());
            String objectPath = String.format("reports/zalog/auto/%s/%s/%s", year, month, fileNameForMinio);

            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(property.getBucket())
                            .object(objectPath)
                            .stream(new ByteArrayInputStream(bytes), bytes.length, -1)
                            .contentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                            .build()
            );

            String url = String.format("%s/%s/%s", property.getUrl(), property.getBucket(), objectPath);
            log.info("Excel file uploaded to MinIO: {}", url);
            return url;

        } catch (Exception e) {
            log.error("Failed to upload Excel to MinIO", e);
            throw new RuntimeException("Failed to upload Excel file to MinIO", e);
        }
    }
}
package uz.davrbank.schotoborotservice.aggregate.service.impl;

import io.minio.GetObjectArgs;
import io.minio.ListObjectsArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.Result;
import io.minio.errors.ErrorResponseException;
import io.minio.messages.Item;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uz.davrbank.schotoborotservice.aggregate.config.MinIOProperty;
import uz.davrbank.schotoborotservice.aggregate.filter.SchotoborotFilter;
import uz.davrbank.schotoborotservice.aggregate.service.MinioService;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class MinioServiceImpl implements MinioService {

    MinioClient minioClient;
    MinIOProperty minioProperty;
    private static final String CSV_BASE_PATH = "schotoborot-service/reports/csv";
    private static final String EXCEL_BASE_PATH = "schotoborot-service/reports/excel";

    @Override
    public String createExcel(SchotoborotFilter filter,
                              byte[] bytes) {
        if (bytes == null) {
            throw new IllegalArgumentException("Data (bytes) can not be null");
        }

        log.info("Saving excel to MinIO, startDate: {}, endDate: {}, account: {}",
                filter.getStartDate(), filter.getEndDate(), filter.getAccount());

        try {
            ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Asia/Tashkent"));
            String fileName;
            if ((filter.getStartDate() == null || filter.getStartDate().isBlank()) && (filter.getEndDate() == null || filter.getEndDate().isBlank())) {
                fileName = "Schotoborot_All.xlsx";
            } else {
                fileName = String.format(
                        "Schotoborot_Report_%s_to_%s.xlsx",
                        filter.getStartDate(), filter.getEndDate()
                );
            }
            String year = String.valueOf(now.getYear());
            String month = String.format("%02d", now.getMonthValue());
            String objectPath = String.format("reports/schot/oborot/%s/%s/%s", year, month, fileName);

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

    @Override
    public Optional<byte[]> getCsvZipIfExists(String folderName) {
        String objectPath = buildCsvObjectPath(folderName);
        try (InputStream stream = minioClient.getObject(
                GetObjectArgs.builder()
                        .bucket(minioProperty.getBucket())
                        .object(objectPath)
                        .build());
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            stream.transferTo(baos);
            return Optional.of(baos.toByteArray());
        } catch (ErrorResponseException e) {
            if (isNotFound(e)) {
                return Optional.empty();
            }
            log.error("Failed to download CSV zip from MinIO: {}", e.getMessage(), e);
            throw new RuntimeException("Error downloading CSV zip from MinIO", e);
        } catch (Exception e) {
            log.error("Failed to download CSV zip from MinIO: {}", e.getMessage(), e);
            throw new RuntimeException("Error downloading CSV zip from MinIO", e);
        }
    }

    @Override
    public void createCsvZip(String folderName, byte[] bytes) {
        if (bytes == null) {
            throw new IllegalArgumentException("Data (bytes) can not be null");
        }
        String objectPath = buildCsvObjectPath(folderName);
        try {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(minioProperty.getBucket())
                            .object(objectPath)
                            .stream(new ByteArrayInputStream(bytes), bytes.length, -1)
                            .contentType("application/zip")
                            .build()
            );
            log.info("CSV zip uploaded to MinIO: {}", objectPath);
        } catch (Exception e) {
            log.error("Failed to upload CSV zip to MinIO: {}", e.getMessage(), e);
            throw new RuntimeException("Error uploading CSV zip to MinIO", e);
        }
    }

    @Override
    public void deleteCsvZipIfExists(String folderName) {
        String objectPath = buildCsvObjectPath(folderName);
        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(minioProperty.getBucket())
                            .object(objectPath)
                            .build()
            );
        } catch (ErrorResponseException e) {
            if (isNotFound(e)) {
                return;
            }
            log.error("Failed to delete CSV zip from MinIO: {}", e.getMessage(), e);
            throw new RuntimeException("Error deleting CSV zip from MinIO", e);
        } catch (Exception e) {
            log.error("Failed to delete CSV zip from MinIO: {}", e.getMessage(), e);
            throw new RuntimeException("Error deleting CSV zip from MinIO", e);
        }
    }

    @Override
    public void uploadCsvObject(String folderName, String fileName, byte[] bytes) {
        if (bytes == null) {
            throw new IllegalArgumentException("Data (bytes) can not be null");
        }
        String objectPath = buildCsvObjectPath(folderName, fileName);
        try {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(minioProperty.getBucket())
                            .object(objectPath)
                            .stream(new ByteArrayInputStream(bytes), bytes.length, -1)
                            .contentType("text/csv")
                            .build()
            );
        } catch (Exception e) {
            log.error("Failed to upload CSV to MinIO: {}", e.getMessage(), e);
            throw new RuntimeException("Error uploading CSV to MinIO", e);
        }
    }


    @Override
    public List<String> listCsvObjects(String folderName) {
        String prefix = buildCsvFolderPrefix(folderName);
        List<String> objects = new ArrayList<>();
        try {
            Iterable<Result<Item>> results = minioClient.listObjects(
                    ListObjectsArgs.builder()
                            .bucket(minioProperty.getBucket())
                            .prefix(prefix)
                            .recursive(true)
                            .build()
            );
            for (Result<Item> result : results) {
                Item item = result.get();
                if (item != null && item.objectName() != null) {
                    objects.add(item.objectName());
                }
            }
        } catch (Exception e) {
            log.error("Failed to list CSV objects from MinIO: {}", e.getMessage(), e);
            throw new RuntimeException("Error listing CSV objects from MinIO", e);
        }
        objects.sort(Comparator.naturalOrder());
        return objects;
    }

    @Override
    public InputStream getCsvObjectStream(String objectPath) {
        try {
            return minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(minioProperty.getBucket())
                            .object(objectPath)
                            .build()
            );
        } catch (Exception e) {
            log.error("Failed to read CSV object from MinIO: {}", e.getMessage(), e);
            throw new RuntimeException("Error reading CSV object from MinIO", e);
        }
    }

    @Override
    public void deleteCsvObjects(String folderName) {
        List<String> objects = listCsvObjects(folderName);
        for (String objectPath : objects) {
            try {
                minioClient.removeObject(
                        RemoveObjectArgs.builder()
                                .bucket(minioProperty.getBucket())
                                .object(objectPath)
                                .build()
                );
            } catch (ErrorResponseException e) {
                if (isNotFound(e)) {
                    continue;
                }
                log.error("Failed to delete CSV object from MinIO: {}", e.getMessage(), e);
                throw new RuntimeException("Error deleting CSV object from MinIO", e);
            } catch (Exception e) {
                log.error("Failed to delete CSV object from MinIO: {}", e.getMessage(), e);
                throw new RuntimeException("Error deleting CSV object from MinIO", e);
            }
        }
    }

    @Override
    public Optional<byte[]> getExcelZipIfExists(String folderName) {
        String objectPath = buildExcelZipObjectPath(folderName);
        try (InputStream stream = minioClient.getObject(
                GetObjectArgs.builder()
                        .bucket(minioProperty.getBucket())
                        .object(objectPath)
                        .build());
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            stream.transferTo(baos);
            return Optional.of(baos.toByteArray());
        } catch (ErrorResponseException e) {
            if (isNotFound(e)) {
                return Optional.empty();
            }
            log.error("Failed to download Excel zip from MinIO: {}", e.getMessage(), e);
            throw new RuntimeException("Error downloading Excel zip from MinIO", e);
        } catch (Exception e) {
            log.error("Failed to download Excel zip from MinIO: {}", e.getMessage(), e);
            throw new RuntimeException("Error downloading Excel zip from MinIO", e);
        }
    }

    @Override
    public void createExcelZip(String folderName, byte[] bytes) {
        if (bytes == null) {
            throw new IllegalArgumentException("Data (bytes) can not be null");
        }
        String objectPath = buildExcelZipObjectPath(folderName);
        try {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(minioProperty.getBucket())
                            .object(objectPath)
                            .stream(new ByteArrayInputStream(bytes), bytes.length, -1)
                            .contentType("application/zip")
                            .build()
            );
            log.info("Excel zip uploaded to MinIO: {}", objectPath);
        } catch (Exception e) {
            log.error("Failed to upload Excel zip to MinIO: {}", e.getMessage(), e);
            throw new RuntimeException("Error uploading Excel zip to MinIO", e);
        }
    }

    @Override
    public void deleteExcelZipIfExists(String folderName) {
        String objectPath = buildExcelZipObjectPath(folderName);
        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(minioProperty.getBucket())
                            .object(objectPath)
                            .build()
            );
        } catch (ErrorResponseException e) {
            if (isNotFound(e)) {
                return;
            }
            log.error("Failed to delete Excel zip from MinIO: {}", e.getMessage(), e);
            throw new RuntimeException("Error deleting Excel zip from MinIO", e);
        } catch (Exception e) {
            log.error("Failed to delete Excel zip from MinIO: {}", e.getMessage(), e);
            throw new RuntimeException("Error deleting Excel zip from MinIO", e);
        }
    }

    @Override
    public void uploadExcelObject(String folderName, String fileName, byte[] bytes) {
        if (bytes == null) {
            throw new IllegalArgumentException("Data (bytes) can not be null");
        }
        String objectPath = buildExcelObjectPath(folderName, fileName);
        try {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(minioProperty.getBucket())
                            .object(objectPath)
                            .stream(new ByteArrayInputStream(bytes), bytes.length, -1)
                            .contentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                            .build()
            );
        } catch (Exception e) {
            log.error("Failed to upload Excel to MinIO: {}", e.getMessage(), e);
            throw new RuntimeException("Error uploading Excel to MinIO", e);
        }
    }


    @Override
    public List<String> listExcelObjects(String folderName) {
        String prefix = buildExcelFolderPrefix(folderName);
        List<String> objects = new ArrayList<>();
        try {
            Iterable<Result<Item>> results = minioClient.listObjects(
                    ListObjectsArgs.builder()
                            .bucket(minioProperty.getBucket())
                            .prefix(prefix)
                            .recursive(true)
                            .build()
            );
            for (Result<Item> result : results) {
                Item item = result.get();
                if (item != null && item.objectName() != null) {
                    objects.add(item.objectName());
                }
            }
        } catch (Exception e) {
            log.error("Failed to list Excel objects from MinIO: {}", e.getMessage(), e);
            throw new RuntimeException("Error listing Excel objects from MinIO", e);
        }
        objects.sort(Comparator.naturalOrder());
        return objects;
    }

    @Override
    public InputStream getExcelObjectStream(String objectPath) {
        try {
            return minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(minioProperty.getBucket())
                            .object(objectPath)
                            .build()
            );
        } catch (Exception e) {
            log.error("Failed to read Excel object from MinIO: {}", e.getMessage(), e);
            throw new RuntimeException("Error reading Excel object from MinIO", e);
        }
    }

    @Override
    public void deleteExcelObjects(String folderName) {
        List<String> objects = listExcelObjects(folderName);
        for (String objectPath : objects) {
            try {
                minioClient.removeObject(
                        RemoveObjectArgs.builder()
                                .bucket(minioProperty.getBucket())
                                .object(objectPath)
                                .build()
                );
            } catch (ErrorResponseException e) {
                if (isNotFound(e)) {
                    continue;
                }
                log.error("Failed to delete Excel object from MinIO: {}", e.getMessage(), e);
                throw new RuntimeException("Error deleting Excel object from MinIO", e);
            } catch (Exception e) {
                log.error("Failed to delete Excel object from MinIO: {}", e.getMessage(), e);
                throw new RuntimeException("Error deleting Excel object from MinIO", e);
            }
        }
    }

    private String buildCsvObjectPath(String folderName) {
        return String.format("%s/%s.zip", CSV_BASE_PATH, folderName);
    }

    private String buildCsvObjectPath(String folderName, String fileName) {
        return String.format("%s/%s/%s", CSV_BASE_PATH, folderName, fileName);
    }

    private String buildCsvFolderPrefix(String folderName) {
        return String.format("%s/%s/", CSV_BASE_PATH, folderName);
    }

    private String buildExcelZipObjectPath(String folderName) {
        return String.format("%s/%s.zip", EXCEL_BASE_PATH, folderName);
    }

    private String buildExcelObjectPath(String folderName, String fileName) {
        return String.format("%s/%s/%s", EXCEL_BASE_PATH, folderName, fileName);
    }

    private String buildExcelFolderPrefix(String folderName) {
        return String.format("%s/%s/", EXCEL_BASE_PATH, folderName);
    }

    private boolean isNotFound(ErrorResponseException e) {
        return e != null
                && e.errorResponse() != null
                && ("NoSuchKey".equalsIgnoreCase(e.errorResponse().code())
                || "NoSuchObject".equalsIgnoreCase(e.errorResponse().code()));
    }
}

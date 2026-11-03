package uz.davrbank.userinfobase.service;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import uz.davrbank.userinfobase.exceptions.GeneralException;
import uz.davrbank.userinfobase.properties.MinioProperties;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileStorageService {

    private static final long DEFAULT_MULTIPART_PART_SIZE = 10L * 1024L * 1024L;

    private static final int MAX_RETRIES = 3;
    private static final long INITIAL_BACKOFF_MS = 2_000L;
    private static final double BACKOFF_MULTIPLIER = 2.0;

    private final MinioClient minioClient;
    private final MinioProperties minioProperties;

    public String upload(byte[] fileBytes, String fileName) {
        String objectName = UUID.randomUUID() + "_" + fileName;
        log.info("MINIO UPLOAD START object={}, fileName={}, size={} bytes, bucket={}",
                objectName, fileName, fileBytes.length, minioProperties.getBucket());
        putObjectWithRetry(fileBytes, objectName, fileBytes.length, -1);
        return objectName;
    }

    public String uploadAs(byte[] fileBytes, String objectName) {
        log.info("MINIO UPLOAD START object={}, size={} bytes, bucket={}",
                objectName, fileBytes.length, minioProperties.getBucket());
        putObjectWithRetry(fileBytes, objectName, fileBytes.length, -1);
        return objectName;
    }

    public byte[] download(String objectName) {
        log.info("MINIO DOWNLOAD START object={}, bucket={}", objectName, minioProperties.getBucket());
        return getObjectWithRetry(objectName, stream -> {
            byte[] data = stream.readAllBytes();
            log.info("MINIO DOWNLOAD OK object={}, size={} bytes", objectName, data.length);
            return data;
        });
    }

    public InputStream openDownloadStream(String objectName) {
        log.info("MINIO STREAM START object={}, bucket={}", objectName, minioProperties.getBucket());
        return getObjectWithRetry(objectName, stream -> {
            log.info("MINIO STREAM OK object={}", objectName);
            return stream;
        });
    }

    public void delete(String objectName) {
        log.info("MINIO DELETE object={}, bucket={}", objectName, minioProperties.getBucket());
        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(minioProperties.getBucket())
                            .object(objectName)
                            .build()
            );
            log.info("MINIO DELETE OK object={}", objectName);
        } catch (Exception e) {
            log.error("MINIO DELETE FAILED object={}, bucket={}, error={}, exceptionClass={}",
                    objectName, minioProperties.getBucket(), e.getMessage(), e.getClass().getSimpleName());
            throw new GeneralException("Error deleting file from MinIO", e);
        }
    }

    @Async
    public void deleteAsync(List<String> objectNames) {
        log.info("MINIO DELETE ASYNC count={}", objectNames.size());
        objectNames.forEach(this::delete);
    }

    public String upload(InputStream inputStream, long fileSize, String fileName) {
        String objectName = UUID.randomUUID() + "_" + fileName;
        log.info("MINIO UPLOAD START object={}, fileName={}, declaredSize={} bytes, bucket={}",
                objectName, fileName, fileSize, minioProperties.getBucket());
        byte[] data = bufferStream(inputStream, objectName);
        putObjectWithRetry(data, objectName, data.length, data.length > 0 ? -1 : DEFAULT_MULTIPART_PART_SIZE);
        return objectName;
    }

    public String uploadAs(InputStream inputStream, long fileSize, String objectName) {
        log.info("MINIO UPLOAD START object={}, declaredSize={} bytes, bucket={}",
                objectName, fileSize, minioProperties.getBucket());
        byte[] data = bufferStream(inputStream, objectName);
        if (data.length != fileSize) {
            log.warn("MINIO UPLOAD SIZE MISMATCH object={}, declaredSize={}, actualSize={}",
                    objectName, fileSize, data.length);
        }
        putObjectWithRetry(data, objectName, data.length, data.length > 0 ? -1 : DEFAULT_MULTIPART_PART_SIZE);
        return objectName;
    }

    public String uploadUnknownSize(InputStream inputStream, String fileName) {
        String objectName = UUID.randomUUID() + "_" + fileName;
        log.info("MINIO UPLOAD START object={}, fileName={}, size=unknown, bucket={}",
                objectName, fileName, minioProperties.getBucket());
        byte[] data = bufferStream(inputStream, objectName);
        log.info("MINIO UPLOAD BUFFERED object={}, actualSize={} bytes", objectName, data.length);
        putObjectWithRetry(data, objectName, data.length, DEFAULT_MULTIPART_PART_SIZE);
        return objectName;
    }


    @FunctionalInterface
    private interface StreamReader<T> {
        T read(InputStream stream) throws Exception;
    }

    private <T> T getObjectWithRetry(String objectName, StreamReader<T> reader) {
        int attempt = 0;
        long backoff = INITIAL_BACKOFF_MS;
        long totalStart = System.currentTimeMillis();

        while (true) {
            attempt++;
            long attemptStart = System.currentTimeMillis();
            try {
                InputStream stream = minioClient.getObject(
                        GetObjectArgs.builder()
                                .bucket(minioProperties.getBucket())
                                .object(objectName)
                                .build()
                );
                T result = reader.read(stream);
                if (attempt > 1) {
                    log.info("MINIO GET OK object={}, attempt={}/{}, totalDuration={}ms",
                            objectName, attempt, MAX_RETRIES, System.currentTimeMillis() - totalStart);
                }
                return result;
            } catch (Exception e) {
                long attemptDuration = System.currentTimeMillis() - attemptStart;
                boolean retryable = isRetryable(e);

                if (attempt >= MAX_RETRIES || !retryable) {
                    long totalDuration = System.currentTimeMillis() - totalStart;
                    log.error("MINIO GET FAILED object={}, attempt={}/{}, retryable={}, "
                                    + "attemptDuration={}ms, totalDuration={}ms, error={}, exceptionClass={}",
                            objectName, attempt, MAX_RETRIES, retryable,
                            attemptDuration, totalDuration, e.getMessage(), e.getClass().getName());
                    throw new GeneralException("Error reading file from MinIO", e);
                }

                log.warn("MINIO GET RETRY object={}, attempt={}/{}, attemptDuration={}ms, "
                                + "backoff={}ms, error={}, exceptionClass={}",
                        objectName, attempt, MAX_RETRIES, attemptDuration, backoff,
                        e.getMessage(), e.getClass().getSimpleName());
                sleep(backoff);
                backoff = (long) (backoff * BACKOFF_MULTIPLIER);
            }
        }
    }

    private void putObjectWithRetry(byte[] data, String objectName, long streamSize, long partSize) {
        int attempt = 0;
        long backoff = INITIAL_BACKOFF_MS;
        long totalStart = System.currentTimeMillis();

        while (true) {
            attempt++;
            long attemptStart = System.currentTimeMillis();
            try (InputStream stream = new ByteArrayInputStream(data)) {
                minioClient.putObject(
                        PutObjectArgs.builder()
                                .bucket(minioProperties.getBucket())
                                .object(objectName)
                                .stream(stream, streamSize, partSize)
                                .build()
                );
                long totalDuration = System.currentTimeMillis() - totalStart;
                log.info("MINIO UPLOAD OK object={}, size={} bytes, attempt={}/{}, duration={}ms, totalDuration={}ms",
                        objectName, data.length, attempt, MAX_RETRIES,
                        System.currentTimeMillis() - attemptStart, totalDuration);
                return;
            } catch (Exception e) {
                long attemptDuration = System.currentTimeMillis() - attemptStart;
                boolean retryable = isRetryable(e);

                if (attempt >= MAX_RETRIES || !retryable) {
                    long totalDuration = System.currentTimeMillis() - totalStart;
                    log.error("MINIO UPLOAD FAILED object={}, size={} bytes, attempt={}/{}, "
                                    + "retryable={}, attemptDuration={}ms, totalDuration={}ms, "
                                    + "error={}, exceptionClass={}",
                            objectName, data.length, attempt, MAX_RETRIES,
                            retryable, attemptDuration, totalDuration,
                            e.getMessage(), e.getClass().getName());
                    throw new GeneralException("Error uploading file to MinIO", e);
                }

                log.warn("MINIO UPLOAD RETRY object={}, size={} bytes, attempt={}/{}, "
                                + "attemptDuration={}ms, backoff={}ms, error={}, exceptionClass={}",
                        objectName, data.length, attempt, MAX_RETRIES,
                        attemptDuration, backoff,
                        e.getMessage(), e.getClass().getSimpleName());
                sleep(backoff);
                backoff = (long) (backoff * BACKOFF_MULTIPLIER);
            }
        }
    }

    private boolean isRetryable(Exception e) {
        String msg = e.getMessage();
        if (msg == null) {
            return false;
        }
        String lower = msg.toLowerCase();
        return lower.contains("reduce your request rate")
                || lower.contains("retry")
                || lower.contains("slow down")
                || lower.contains("service unavailable")
                || lower.contains("too many requests")
                || lower.contains("connection reset")
                || lower.contains("connection refused");
    }

    private byte[] bufferStream(InputStream inputStream, String objectName) {
        try {
            byte[] data = inputStream.readAllBytes();
            log.debug("MINIO BUFFER OK object={}, size={} bytes", objectName, data.length);
            return data;
        } catch (Exception e) {
            log.error("MINIO BUFFER FAILED object={}, error={}, exceptionClass={}",
                    objectName, e.getMessage(), e.getClass().getSimpleName());
            throw new GeneralException("Error reading input stream for upload", e);
        }
    }

    private void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
            throw new GeneralException("Upload retry interrupted");
        }
    }
}

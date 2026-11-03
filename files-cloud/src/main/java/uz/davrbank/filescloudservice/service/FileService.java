package uz.davrbank.filescloudservice.service;

import io.minio.GetObjectArgs;
import io.minio.ListObjectsArgs;
import io.minio.MinioClient;
import io.minio.Result;
import io.minio.messages.Item;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uz.davrbank.filescloudservice.aggrecation.dto.MinioProperty;
import uz.davrbank.filescloudservice.aggrecation.dto.MonthFilesDTO;
import uz.davrbank.filescloudservice.aggrecation.enums.Applications;
import uz.davrbank.filescloudservice.service.interfaces.FileInterface;

import java.io.InputStream;
import java.util.Map;
import java.util.TreeMap;

@Slf4j
@Service
public class FileService implements FileInterface {
    private final MinioClient minioClient;
    private final MinioProperty minioProperty;

    public FileService(MinioClient minioClient, MinioProperty minioProperty) {
        this.minioClient = minioClient;
        this.minioProperty = minioProperty;
    }

    @Override
    public Map<String, Map<String, MonthFilesDTO>> getAllFiles(Applications service) {
        String basePath = service.getPath();
        return buildFileMap(basePath);
    }

    @Override
    public ResponseEntity<Resource> getFile(String filePath) {
        try {
            InputStream stream = minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(minioProperty.getBucket())
                            .object(filePath)
                            .build()
            );

            byte[] fileBytes = stream.readAllBytes();
            log.info("File: {}", filePath);
            ByteArrayResource resource = new ByteArrayResource(fileBytes);
            // Извлекаем имя файла без пути
            String fileName = filePath.substring(filePath.lastIndexOf("/") + 1);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                    .contentLength(fileBytes.length)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);

        } catch (Exception e) {
            log.error(" Error upload file: filePath={}, message={}", filePath, e.getMessage());
            throw new RuntimeException("Error upload file: " + e.getMessage(), e);
        }
    }


    public Map<String, Map<String, MonthFilesDTO>> buildFileMap(String basePath) {
        String[] splits = basePath.split("/");
        Map<String, Map<String, MonthFilesDTO>> result = new TreeMap<>();

        try {
            Iterable<Result<Item>> objects = minioClient.listObjects(
                    ListObjectsArgs.builder()
                            .bucket(minioProperty.getBucket())
                            .prefix(basePath)
                            .recursive(true)
                            .build()
            );

            for (Result<Item> resultItem : objects) {
                Item item = resultItem.get();
                if (!item.isDir()) {
                    String objectName = item.objectName();
                    String[] parts = objectName.split("/");
                    if (parts.length >= 3) {
                        String year = parts[splits.length];
                        String month = parts[splits.length +1];
                        String fileName = parts[parts.length - 1];
                        String relativePath = basePath +"/"+ year + "/" + month+"/";
                        Map<String, MonthFilesDTO> months = result.computeIfAbsent(year, k -> new TreeMap<>());

                        MonthFilesDTO dto = months.computeIfAbsent(month, k -> new MonthFilesDTO(relativePath));
                        dto.addFileName(fileName);
                    }
                }
            }
        } catch (Exception e) {
            log.info("Error getting file paths from MinIO: message={}, cause={}", e.getMessage(), e.getCause(), e);
            throw new RuntimeException("Error getting file paths from MinIO", e);
        }
        return result;
    }
}
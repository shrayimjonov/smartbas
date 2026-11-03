package uz.davrbank.usermanualservice.aggregate.service;

import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import io.minio.*;
import io.minio.messages.Item;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import uz.davrbank.usermanualservice.aggregate.domain.UserManualSection;
import uz.davrbank.usermanualservice.aggregate.domain.UserManualVersion;
import uz.davrbank.usermanualservice.aggregate.dto.FileDTO;
import uz.davrbank.usermanualservice.aggregate.dto.FileUploadDTO;
import uz.davrbank.usermanualservice.aggregate.model.ResultData;
import uz.davrbank.usermanualservice.aggregate.repository.UserManualSectionRepository;
import uz.davrbank.usermanualservice.aggregate.repository.UserManualVersionRepository;
import uz.davrbank.usermanualservice.config.ApplicationProperties;
import uz.davrbank.usermanualservice.exception.condition.BadRequestException;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Author: Shukhratjon Rayimjonov
 * Date: 24.11.2025
 * Time: 16.42
 * Project: usermanual-service
 */

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class FileService {

    MinioClient minioClient;
    ApplicationProperties applicationProperties;
    UserManualVersionRepository userManualVersionRepository;
    UserManualSectionRepository userManualSectionRepository;
    Parser parser;
    HtmlRenderer htmlRenderer;

    public ResultData<List<FileDTO>> uploadCommonFiles(List<MultipartFile> files) {
        if (files == null || files.isEmpty()) {
            throw new RuntimeException("files is required and must not be empty");
        }
        List<FileDTO> result = files.stream().map(file -> {
            String fileName = file.getOriginalFilename() != null ? file.getOriginalFilename() : "file";
            String objectName = applicationProperties.getMinIo().getDocumentBaseUrl() + "/" + fileName;
            return uploadToMinIOMultipart(file, fileName, objectName);
        }).collect(Collectors.toList());
        return ResultData.ok(result);
    }

    @Transactional(readOnly = true)
    public ResultData<FileDTO> uploadFile(FileUploadDTO fileUploadDTO) {
        return userManualVersionRepository.findById(fileUploadDTO.versionId()).map(version -> {
            String path = resolve(version, fileUploadDTO.language());
            String objectName = path + "/" + fileUploadDTO.fileName();
            return ResultData.ok(this.uploadToMinIO(fileUploadDTO.contentBase64(), fileUploadDTO.fileName(), objectName));
        }).orElseThrow(() -> new BadRequestException("User manual version not found"));
    }

    public FileDTO uploadToMinIO(String file, String fileName, String objectName) {
        try {
            if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(applicationProperties.getMinIo().getBucket()).build())) {
                throw new RuntimeException("Bucket does not exist");
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

        try (ByteArrayInputStream stream = new ByteArrayInputStream(file.getBytes())) {
            log.info("FILENAME: {} -------------------------------------", fileName);
            log.info("OBJECT KEY: {} -------------------------------------", objectName);
            minioClient.putObject(
                    PutObjectArgs
                            .builder()
                            .bucket(applicationProperties.getMinIo().getBucket())
                            .object(objectName)
                            .stream(stream, file.length(), -1)
                            .build()
            );
            log.info("File uploaded successfully");
            return new FileDTO(fileName, objectName);
        } catch (Exception e) {
            log.error("File upload error: {}", e.getMessage());
            throw new RuntimeException("File upload error", e);
        }
    }

    @Transactional(readOnly = true)
    public ResultData<FileDTO> uploadFile(Long versionId, String language, MultipartFile file) {
        return userManualVersionRepository.findById(versionId).map(version -> {
            String path = resolve(version, language);
            String fileName = file.getOriginalFilename() != null ? file.getOriginalFilename() : "file" + UUID.randomUUID();
            String objectName = path + "/" + fileName;
            return ResultData.ok(this.uploadToMinIOMultipart(file, fileName, objectName));
        }).orElseThrow(() -> new BadRequestException("User manual version not found"));
    }

    private FileDTO uploadToMinIOMultipart(MultipartFile file, String fileName, String objectName) {
        try {
            if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(applicationProperties.getMinIo().getBucket()).build())) {
                throw new RuntimeException("Bucket does not exist");
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        try (InputStream stream = file.getInputStream()) {
            log.info("FILENAME: {} -------------------------------------", fileName);
            log.info("OBJECT KEY: {} -------------------------------------", objectName);
            PutObjectArgs.Builder builder = PutObjectArgs
                    .builder()
                    .bucket(applicationProperties.getMinIo().getBucket())
                    .object(objectName)
                    .stream(stream, file.getSize(), -1);
            String contentType = file.getContentType();
            if (contentType != null && !contentType.isBlank()) {
                builder.contentType(contentType);
            }
            minioClient.putObject(builder.build());
            log.info("File uploaded successfully");
            return new FileDTO(fileName, objectName);
        } catch (Exception e) {
            log.error("File upload error: {}", e.getMessage());
            throw new RuntimeException("File upload error", e);
        }
    }

    @Transactional(readOnly = true)
    public String get(Long versionId, String language) {
        return userManualVersionRepository.findById(versionId).map(userManualVersion -> {

                    String path = resolve(userManualVersion, language);
                    try {
                        Iterable<Result<Item>> results = minioClient.listObjects(
                                ListObjectsArgs.builder()
                                        .bucket(applicationProperties.getMinIo().getBucket())
                                        .recursive(true)
                                        .prefix(path).build()
                        );
                        if (!results.iterator().hasNext()) {
                            throw new BadRequestException("No files found");
                        }
                        Item markdownItem = null;
                        for (Result<Item> result : results) {
                            Item item = result.get();

                            if (item.isDir()) {
                                continue;
                            }

                            if (item.objectName().endsWith(".md")) {
                                markdownItem = item;
                                break;
                            }
                        }
                        if (markdownItem == null) {
                            throw new BadRequestException("Markdown file not found");
                        }
                        GetObjectResponse response = minioClient.getObject(
                                GetObjectArgs.builder()
                                        .bucket(applicationProperties.getMinIo().getBucket())
                                        .object(markdownItem.objectName())
                                        .build()

                        );
                        String markdown = new String(response.readAllBytes(), StandardCharsets.UTF_8);
                        String resolvedContent = markdown.replace("{{IMAGE_BASE_URL}}", applicationProperties.getMinIo().getDocumentBaseUrl());
                        return htmlRenderer.render(parser.parse(resolvedContent));
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                })
                .orElseThrow(() -> new BadRequestException("Version with id not found"));
    }

    public void recursiveIteration(UserManualSection section, List<String> manualSections) {
        if (section != null && section.getParentId() != null) {
            userManualSectionRepository.findById(section.getParentId()).ifPresent(userManualSection -> {
                manualSections.add(userManualSection.getName());
                recursiveIteration(userManualSection, manualSections);
            });
        }
    }

    public String resolve(UserManualVersion userManualVersion, String language) {
        List<String> paths = new ArrayList<>();
        paths.add(language);
        paths.add(userManualVersion.getVersionNumber());
        paths.add(userManualVersion.getSection().getName());
        recursiveIteration(userManualVersion.getSection(), paths);
        Collections.reverse(paths);
        return String.join("/", paths);
    }
}
package uz.davrbank.userinfobase.V2.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;
import org.apache.tika.mime.MimeType;
import org.apache.tika.mime.MimeTypeException;
import org.apache.tika.mime.MimeTypes;
import org.springframework.stereotype.Service;
import uz.davrbank.userinfobase.V2.dto.AttachmentDTO;
import uz.davrbank.userinfobase.V2.dto.LoanAttachmentDTO;
import uz.davrbank.userinfobase.V2.entity.Attachment;
import uz.davrbank.userinfobase.dto.request.elma.FileUploadBase64V2Request;
import uz.davrbank.userinfobase.dto.response.ResultData;
import uz.davrbank.userinfobase.dto.response.files.FileUploadResponse;
import uz.davrbank.userinfobase.enums.ResponseStatus;
import uz.davrbank.userinfobase.service.FileStorageService;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HexFormat;
import java.util.List;

@Service
@Slf4j
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class AttachmentUploadService {

    private static final Tika TIKA = new Tika();
    private static final String DEFAULT_CONTENT_TYPE = "application/octet-stream";

    AttachmentService attachmentService;
    LoanAttachmentService loanAttachmentService;
    FileStorageService fileStorageService;
    private static final String EXTERNAL_FILE_DOWNLOAD_URL_PREFIX =
            "https://internal.example.com/back/api/external-files/download/";


    public ResultData<List<FileUploadResponse>> uploadElma(FileUploadBase64V2Request request) {

        if (request.getLoanExtId() == null || request.getLoanExtId().isEmpty()) {
            return new ResultData<>(false, ResponseStatus.INVALID_REQUEST, "loanExtId is null");
        }

        if (request.getBranchId() == null || request.getBranchId().isEmpty()) {
            return new ResultData<>(false, ResponseStatus.INVALID_REQUEST, "branchId is null");
        }

        if (request.getFiles() == null || request.getFiles().isEmpty()) {
            return new ResultData<>(false, ResponseStatus.INVALID_REQUEST, "Empty file list");
        }
        List<FileUploadResponse> result = new ArrayList<>();
        for (String file : request.getFiles()) {
            DecodedElmaFile decodedFile = decodeElmaFile(file);
            byte[] fileBytes = decodedFile.fileBytes();
            String fileHash = this.getFileName(fileBytes);
            String originalName = fileHash + getFileExtension(decodedFile.contentType());
            String objectName = "ELMA_" + originalName;
            Attachment attachment = attachmentService.get(objectName);
            if (attachment == null) {
                attachment = attachmentService.get("ELMA_" + fileHash);
            }
            try {
                if (attachment == null) {
                    long fileSize = fileBytes.length;
                    fileStorageService.uploadAs(fileBytes, objectName);
                    attachment = attachmentService.create(toAttachmentDTO(objectName, originalName, fileSize, decodedFile.contentType()));
                    String url = EXTERNAL_FILE_DOWNLOAD_URL_PREFIX + attachment.getId();
                    attachment.setAttachmentUrl(url);
                    attachmentService.saveAttachment(attachment);
                } else {
                    attachment = attachmentService.create(toAttachmentDTO(
                            attachment.getObjectName(),
                            originalName,
                            attachment.getFileSize() == null ? (long) fileBytes.length : attachment.getFileSize(),
                            decodedFile.contentType()
                    ));
                }
                loanAttachmentService.create(new LoanAttachmentDTO(attachment.getId(), request.getBranchId(), request.getLoanExtId()));
                FileUploadResponse fileUploadResponse = new FileUploadResponse(
                        null, attachment.getOriginalName(), null, FileUploadResponse.formatFileSize(attachment.getFileSize()), null, LocalDateTime.now()
                );
                result.add(fileUploadResponse);
            } catch (Exception e) {
                log.info("File upload error: {}", e.getMessage());
            }
        }
        return new ResultData<>(result);

    }

    private String getFileName(byte[] fileBytes) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            log.info("SHA-256 algorithm not found", e);
            throw new RuntimeException(e);
        }
        byte[] hash = digest.digest(fileBytes);
        return HexFormat.of().formatHex(hash);
    }

    private AttachmentDTO toAttachmentDTO(String objectName, String originalName, Long fileSize, String contentType) {
        return new AttachmentDTO(
                objectName,
                originalName,
                null,
                "ELMA",
                originalName,
                fileSize,
                contentType
        );
    }

    private DecodedElmaFile decodeElmaFile(String file) {
        String contentTypeFromPrefix = readContentTypeFromDataUri(file);
        if (file.contains(",")) {
            file = file.split(",", 2)[1];
        }
        byte[] fileBytes = Base64.getDecoder().decode(file);
        return new DecodedElmaFile(fileBytes, detectContentType(fileBytes, contentTypeFromPrefix));
    }

    private String readContentTypeFromDataUri(String file) {
        if (file == null || !file.startsWith("data:")) {
            return null;
        }
        int separatorIndex = file.indexOf(',');
        int contentTypeEndIndex = file.indexOf(';');
        if (contentTypeEndIndex < 0 || contentTypeEndIndex > separatorIndex) {
            return null;
        }
        String contentType = file.substring("data:".length(), contentTypeEndIndex).trim();
        return contentType.isBlank() ? null : contentType;
    }

    private String detectContentType(byte[] fileBytes, String contentTypeFromPrefix) {
        if (contentTypeFromPrefix != null && !contentTypeFromPrefix.isBlank()) {
            return contentTypeFromPrefix;
        }

        String contentType = TIKA.detect(fileBytes);
        if (contentType != null && !contentType.isBlank() && !DEFAULT_CONTENT_TYPE.equals(contentType)) {
            return contentType;
        }

        return detectBySignature(fileBytes);
    }

    private String detectBySignature(byte[] fileBytes) {
        if (startsWith(fileBytes, "%PDF-")) {
            return "application/pdf";
        }
        if (startsWith(fileBytes, (byte) 0xFF, (byte) 0xD8, (byte) 0xFF)) {
            return "image/jpeg";
        }
        if (startsWith(fileBytes, (byte) 0x89, (byte) 'P', (byte) 'N', (byte) 'G', (byte) 0x0D, (byte) 0x0A, (byte) 0x1A, (byte) 0x0A)) {
            return "image/png";
        }
        if (startsWith(fileBytes, "GIF87a") || startsWith(fileBytes, "GIF89a")) {
            return "image/gif";
        }
        if (startsWith(fileBytes, "{\\rtf")) {
            return "application/rtf";
        }
        if (startsWith(fileBytes, (byte) 0xD0, (byte) 0xCF, (byte) 0x11, (byte) 0xE0, (byte) 0xA1, (byte) 0xB1, (byte) 0x1A, (byte) 0xE1)) {
            return "application/vnd.ms-office";
        }
        if (startsWith(fileBytes, (byte) 'P', (byte) 'K', (byte) 0x03, (byte) 0x04)) {
            return "application/zip";
        }
        return DEFAULT_CONTENT_TYPE;
    }

    private boolean startsWith(byte[] bytes, String prefix) {
        return startsWith(bytes, prefix.getBytes(StandardCharsets.US_ASCII));
    }

    private boolean startsWith(byte[] bytes, byte... prefix) {
        if (bytes == null || bytes.length < prefix.length) {
            return false;
        }
        for (int i = 0; i < prefix.length; i++) {
            if (bytes[i] != prefix[i]) {
                return false;
            }
        }
        return true;
    }

    private String getFileExtension(String mimeType) {
        try {
            MimeType type = MimeTypes.getDefaultMimeTypes().forName(mimeType);
            return type.getExtension();
        } catch (MimeTypeException e) {
            return "";
        }
    }

    private record DecodedElmaFile(byte[] fileBytes, String contentType) {
    }

}

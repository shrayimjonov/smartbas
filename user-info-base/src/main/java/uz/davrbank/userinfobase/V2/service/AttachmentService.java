package uz.davrbank.userinfobase.V2.service;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import uz.davrbank.userinfobase.V2.dto.AttachmentDTO;
import uz.davrbank.userinfobase.V2.entity.Attachment;
import uz.davrbank.userinfobase.V2.repository.AttachmentRepository;
import uz.davrbank.userinfobase.service.FileStorageService;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AttachmentService {

    private static final String DEFAULT_CONTENT_TYPE = "application/octet-stream";
    private static final Tika TIKA = new Tika();

    FileStorageService fileStorageService;

    private static final String EXTERNAL_FILE_DOWNLOAD_URL_PREFIX =
            "https://internal.example.com/back/api/external-files/download/";

    AttachmentRepository attachmentRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Attachment create(AttachmentDTO attachmentDTO) {

        Attachment attachment = attachmentRepository.findFirstByObjectName(attachmentDTO.getObjectName()).orElseGet(Attachment::new);
        if (attachment.getId() != null) {
            boolean changed = false;
            if (attachment.getFileSize() == null && attachmentDTO.getFileSize() != null) {
                attachment.setFileSize(attachmentDTO.getFileSize());
                changed = true;
            }
            if (shouldUpdateContentType(attachment.getContentType(), attachmentDTO.getContentType())) {
                attachment.setContentType(attachmentDTO.getContentType());
                changed = true;
            }
            if (changed) {
                return attachmentRepository.saveAndFlush(attachment);
            }
            return attachment;
        }
        attachment.setAttachmentUrl(resolveAttachmentUrl(attachmentDTO.getAttachmentUrl(), ""));
        attachment.setSource(resolveSource(attachmentDTO.getSource()));
        attachment.setObjectName(attachmentDTO.getObjectName());
        attachment.setDisplayName(resolveDisplayName(attachmentDTO));
        attachment.setOriginalName(resolveOriginalName(attachmentDTO));
        attachment.setFileSize(attachmentDTO.getFileSize());
        attachment.setContentType(resolveContentType(attachmentDTO.getContentType()));
        Attachment savedAttachment = attachmentRepository.saveAndFlush(attachment);
        if (attachmentDTO.getAttachmentUrl() == null || attachmentDTO.getAttachmentUrl().isBlank()) {
            savedAttachment.setAttachmentUrl(resolveAttachmentUrl(null, savedAttachment.getId().toString()));
            savedAttachment = attachmentRepository.saveAndFlush(savedAttachment);
        }
        return savedAttachment;
    }

    public Attachment get(String objectName) {
        return attachmentRepository.findFirstByObjectName(objectName).orElse(null);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveAttachment(Attachment attachment) {
        attachmentRepository.saveAndFlush(attachment);
    }

    private String resolveAttachmentUrl(String attachmentUrl, String attachmentId) {
        if (attachmentUrl != null && !attachmentUrl.isBlank()) {
            return attachmentUrl;
        }
        return EXTERNAL_FILE_DOWNLOAD_URL_PREFIX + attachmentId;
    }

    private String resolveSource(String source) {
        return source == null || source.isBlank() ? "UNKNOWN" : source;
    }

    private String resolveDisplayName(AttachmentDTO attachmentDTO) {
        if (attachmentDTO.getDisplayName() != null && !attachmentDTO.getDisplayName().isBlank()) {
            return attachmentDTO.getDisplayName();
        }
        return resolveOriginalName(attachmentDTO);
    }

    private String resolveOriginalName(AttachmentDTO attachmentDTO) {
        if (attachmentDTO.getOriginalName() != null && !attachmentDTO.getOriginalName().isBlank()) {
            return attachmentDTO.getOriginalName();
        }
        return attachmentDTO.getObjectName();
    }


    public ResponseEntity<StreamingResponseBody> download(String attachmentId) {
        Attachment attachment = attachmentRepository.findById(UUID.fromString(attachmentId)).orElseThrow(() -> new RuntimeException("File not found with this id: " + attachmentId));
        StreamingResponseBody body = outputStream -> {
            try (InputStream inputStream = fileStorageService.openDownloadStream(attachment.getObjectName())) {
                inputStream.transferTo(outputStream);
            }
        };

        String mimeType = resolveDownloadContentType(attachment);
        ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(mimeType))
                .header(HttpHeaders.CONTENT_DISPOSITION, attachmentHeader(attachment.getOriginalName()));

        if (attachment.getFileSize() != null && attachment.getFileSize() > 0) {
            responseBuilder.contentLength(attachment.getFileSize());
        }

        return responseBuilder.body(body);
    }

    private String resolveContentType(String contentType) {
        return contentType == null || contentType.isBlank() ? DEFAULT_CONTENT_TYPE : contentType;
    }

    private String resolveDownloadContentType(Attachment attachment) {
        String contentType = resolveContentType(attachment.getContentType());
        if (!DEFAULT_CONTENT_TYPE.equals(contentType)) {
            return contentType;
        }

        String detectedContentType = detectContentType(attachment);
        if (!DEFAULT_CONTENT_TYPE.equals(detectedContentType)) {
            attachment.setContentType(detectedContentType);
            attachmentRepository.saveAndFlush(attachment);
            return detectedContentType;
        }

        return contentType;
    }

    private String detectContentType(Attachment attachment) {
        String nameDetectedContentType = URLConnection.guessContentTypeFromName(attachment.getOriginalName());
        if (isResolvedContentType(nameDetectedContentType)) {
            return nameDetectedContentType;
        }

        String signatureContentType = detectContentTypeBySignature(attachment);
        if (isResolvedContentType(signatureContentType)) {
            return signatureContentType;
        }

        try (InputStream inputStream = fileStorageService.openDownloadStream(attachment.getObjectName())) {
            String tikaContentType = TIKA.detect(inputStream, attachment.getOriginalName());
            if (isResolvedContentType(tikaContentType)) {
                return tikaContentType;
            }
        } catch (IOException e) {
            log.warn("Failed to detect attachment content type id={}, objectName={}", attachment.getId(), attachment.getObjectName(), e);
        }

        return DEFAULT_CONTENT_TYPE;
    }

    private String detectContentTypeBySignature(Attachment attachment) {
        try (InputStream inputStream = fileStorageService.openDownloadStream(attachment.getObjectName())) {
            byte[] header = inputStream.readNBytes(8192);
            if (startsWith(header, "%PDF-")) {
                return "application/pdf";
            }
            if (startsWith(header, (byte) 0xFF, (byte) 0xD8, (byte) 0xFF)) {
                return "image/jpeg";
            }
            if (startsWith(header, (byte) 0x89, (byte) 'P', (byte) 'N', (byte) 'G', (byte) 0x0D, (byte) 0x0A, (byte) 0x1A, (byte) 0x0A)) {
                return "image/png";
            }
            if (startsWith(header, "GIF87a") || startsWith(header, "GIF89a")) {
                return "image/gif";
            }
            if (startsWith(header, "{\\rtf")) {
                return "application/rtf";
            }
            if (startsWith(header, (byte) 0xD0, (byte) 0xCF, (byte) 0x11, (byte) 0xE0, (byte) 0xA1, (byte) 0xB1, (byte) 0x1A, (byte) 0xE1)) {
                return "application/vnd.ms-office";
            }
            if (startsWith(header, (byte) 'P', (byte) 'K', (byte) 0x03, (byte) 0x04)) {
                return "application/zip";
            }
        } catch (IOException e) {
            log.warn("Failed to read attachment header id={}, objectName={}", attachment.getId(), attachment.getObjectName(), e);
        }
        return DEFAULT_CONTENT_TYPE;
    }

    private boolean isResolvedContentType(String contentType) {
        return contentType != null && !contentType.isBlank() && !DEFAULT_CONTENT_TYPE.equals(contentType);
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

    private boolean shouldUpdateContentType(String currentContentType, String newContentType) {
        if (newContentType == null || newContentType.isBlank()) {
            return false;
        }
        return currentContentType == null
                || currentContentType.isBlank()
                || DEFAULT_CONTENT_TYPE.equals(currentContentType);
    }

    private String attachmentHeader(String fileName) {
        return ContentDisposition.attachment()
                .filename(fileName, StandardCharsets.UTF_8)
                .build()
                .toString();
    }

    public void deleteFiles() {
        deleteFromMinIO();
        attachmentRepository.deleteAll();
    }

    public void deleteFromMinIO() {
        List<String> objectNames = attachmentRepository.findAll().stream().map(Attachment::getObjectName).toList();
        fileStorageService.deleteAsync(objectNames);
    }
}

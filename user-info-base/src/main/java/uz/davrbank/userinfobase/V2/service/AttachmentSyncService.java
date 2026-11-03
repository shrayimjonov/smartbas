package uz.davrbank.userinfobase.V2.service;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import uz.davrbank.userinfobase.V2.dto.AttachmentDTO;
import uz.davrbank.userinfobase.V2.dto.LoanAttachmentDTO;
import uz.davrbank.userinfobase.V2.entity.Attachment;
import uz.davrbank.userinfobase.V2.repository.LoanAttachmentRepository;
import uz.davrbank.userinfobase.client.DavrhubGatewayClient;
import uz.davrbank.userinfobase.dto.ExternalFileDtoResponse;
import uz.davrbank.userinfobase.dto.ExternalFileRequestDto;
import uz.davrbank.userinfobase.dto.request.DavrhubFileListRequest;
import uz.davrbank.userinfobase.dto.request.elma.ElmaRequestDto;
import uz.davrbank.userinfobase.dto.request.elma.ElmaResponseDto;
import uz.davrbank.userinfobase.dto.response.ResultData;
import uz.davrbank.userinfobase.enums.ResponseStatus;
import uz.davrbank.userinfobase.exceptions.ElmaException;
import uz.davrbank.userinfobase.exceptions.GeneralException;
import uz.davrbank.userinfobase.feign.elma.ElmaFeignClient;
import uz.davrbank.userinfobase.service.FileStorageService;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.SequenceInputStream;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.DigestOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.function.IntSupplier;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AttachmentSyncService {
    private static final String DAVRHUB_FILE_LIST_URI = "/api/v1/davrhub/execute/list/421";
    private static final int STREAM_BUFFER_SIZE = 32;
    private static final String DEFAULT_FILE_NAME = "davrhub-file";
    private static final String DAVRHUB_SOURCE = "NCI";
    private static final String DEFAULT_CONTENT_TYPE = "application/octet-stream";
    private static final Tika TIKA = new Tika();

    @NonFinal
    private static volatile long syncDelayMillis = 30_000L;

    ObjectMapper objectMapper;
    ElmaFeignClient elmaFeignClient;
    DavrhubGatewayClient davrhubGatewayClient;
    AttachmentService attachmentService;
    LoanAttachmentService loanAttachmentService;
    FileStorageService fileStorageService;
    LoanAttachmentRepository loanAttachmentRepository;

    public ResultData<List<ExternalFileDtoResponse>> syncAllFiles(ExternalFileRequestDto request) {
        try {
            log.info("SYNC FILE REQUEST: {}", objectMapper.writeValueAsString(request));
        } catch (JsonProcessingException e) {
            log.error("Object writing error: {}", e.getMessage());
        }

        if (request == null) {
            return new ResultData<>(false, ResponseStatus.INVALID_REQUEST, "Request is null");
        }
        if (request.getBranchId() == null || request.getBranchId().isEmpty()) {
            return new ResultData<>(false, ResponseStatus.INVALID_REQUEST, "Branch Id is null");
        }
        if (request.getLoanId() == null || request.getLoanId().isEmpty()) {
            return new ResultData<>(false, ResponseStatus.INVALID_REQUEST, "Loan Id is null");
        }

        String loanId = request.getLoanId().trim();
        String branchId = request.getBranchId().trim();
        log.info("SYNC START loanId={}, branchId={}", loanId, branchId);

        safeRun(() -> syncElma(loanId, branchId), "ELMA");
        safeRunWithResult(() -> syncDavrhub(loanId, branchId));
        try {
            Thread.sleep(syncDelayMillis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.warn("Sleep interrupted");
        }

        List<ExternalFileDtoResponse> response = toExternalFileList(loanId, branchId);
        log.info("SYNC FINISHED total files={}", response.size());

        return new ResultData<>(response);
    }

    private List<ExternalFileDtoResponse> toExternalFileList(String loanId, String branchId) {
        List<LoanAttachmentRepository.LoanAttachmentFileView> files = loanAttachmentService.getFileViews(loanId, branchId);
        if (files == null || files.isEmpty()) {
            log.info("SYNC RESPONSE QUERY loanId={}, branchId={}, files=0", loanId, branchId);
            return List.of();

        }

        List<ExternalFileDtoResponse> responses = new ArrayList<>();
        long counter = 0;
        for (LoanAttachmentRepository.LoanAttachmentFileView file : files) {
            long currentCounter = ++counter;
            String fileName = resolveResponseFileName(file, loanId, branchId, currentCounter);
            responses.add(new ExternalFileDtoResponse(
                    currentCounter,
                    fileName,
                    ExternalFileDtoResponse.formatFileSize(file.getFileSize()),
                    file.getSource(),
                    fileName,
                    file.getAttachmentUrl()
            ));
        }
        log.info("SYNC RESPONSE QUERY loanId={}, branchId={}, files={}", loanId, branchId, responses.size());
        return responses;
    }

    private String resolveResponseFileName(
            LoanAttachmentRepository.LoanAttachmentFileView file,
            String loanId,
            String branchId,
            long counter
    ) {
        if (DAVRHUB_SOURCE.equals(file.getSource()) && file.getDisplayName() != null && !file.getDisplayName().isBlank()) {
            return file.getDisplayName();
        }

        String source = file.getSource() == null || file.getSource().isBlank() ? "FILE" : file.getSource();
        return source + "_" + loanId + "_" + branchId + "_" + counter;
    }

    private void safeRun(Runnable action, String systemName) {
        try {
            action.run();
            log.info("{} SUCCESS", systemName);
        } catch (Exception e) {
            log.warn("{} GENERAL ERROR: {}", systemName, e.getMessage());
        }
    }

    private int safeRunWithResult(IntSupplier action) {
        try {
            int result = action.getAsInt();
            log.info("{} SUCCESS result={}", "DAVRHUB", result);
            return result;
        } catch (Exception e) {
            log.warn("{} GENERAL ERROR: {}", "DAVRHUB", e.getMessage());
            return 0;
        }
    }

    private void syncElma(String loanId, String branchId) {
        requestToElma(loanId, branchId);
    }

    private void requestToElma(String loanId, String branchId) {
        ElmaRequestDto elmaRequestDto = buildElmaRequest(loanId, branchId);
        try {
            log.info("ELMA request for loanExtID: {}, branchID: {}", loanId, branchId);
            ElmaResponseDto elmaResponseDto = elmaFeignClient.transfer(elmaRequestDto);
            if (elmaResponseDto == null) {
                throw new ElmaException("ELMA response is empty.");
            }
            log.info("ELMA response {}", elmaResponseDto);
        } catch (FeignException e) {
            log.error("ELMA API error: status={}, body={}", e.status(), e.contentUTF8());
            throw new ElmaException("ELMA service returned error: " + e.contentUTF8());
        } catch (Exception e) {
            log.error("ELMA connection error", e);
            throw new ElmaException("Error connecting to ELMA service.");
        }
    }

    private ElmaRequestDto buildElmaRequest(String loanId, String branchId) {
        ElmaRequestDto.MessageContentDto dto = ElmaRequestDto.MessageContentDto
                .builder()
                .LoanId(loanId)
                .branchId(branchId)
                .build();

        return ElmaRequestDto
                .builder()
                .action("getLoanDoc")
                .method("getFile")
                .server("prod")
                .Success("1")
                .MessageType("DataResponse")
                .RetryRequestId("")
                .TransactionRequestID("")
                .ErrorMessage("")
                .InstanceID("")
                .messageContent(dto)
                .build();
    }

    private int syncDavrhub(String loanId, String branchId) {
        Flux<DataBuffer> responseBody = davrhubGatewayClient.stream(
                HttpMethod.POST,
                DAVRHUB_FILE_LIST_URI,
                new DavrhubFileListRequest(loanId, branchId)
        );

        try (InputStream responseStream = toInputStream(responseBody);
             JsonParser parser = objectMapper.getFactory().createParser(responseStream)) {
            int syncedFiles = parseDavrhubResponse(parser, loanId, branchId);
            log.info("DAVRHUB sync finished loanId={}, branchId={}, syncedFiles={}", loanId, branchId, syncedFiles);
            return syncedFiles;
        } catch (Exception e) {
            log.error("Error from davrhub: {}", e.getMessage());
            throw new GeneralException("Error fetching files from Davrhub", e);
        }
    }

    private InputStream toInputStream(Flux<DataBuffer> responseBody) {
        Iterator<DataBuffer> iterator = responseBody.toIterable(STREAM_BUFFER_SIZE).iterator();
        Enumeration<InputStream> streams = new Enumeration<>() {
            @Override
            public boolean hasMoreElements() {
                return iterator.hasNext();
            }

            @Override
            public InputStream nextElement() {
                return iterator.next().asInputStream(true);
            }
        };
        return new SequenceInputStream(streams);
    }

    private int parseDavrhubResponse(JsonParser parser, String loanId, String branchId) throws Exception {
        JsonToken firstToken = parser.nextToken();
        if (firstToken == null) {
            return 0;
        }

        if (firstToken == JsonToken.START_ARRAY) {
            return readFilesArray(parser, loanId, branchId);
        }

        if (firstToken != JsonToken.START_OBJECT) {
            throw new GeneralException("Unknown Davrhub response format");
        }

        int syncedFiles = 0;
        JsonToken token;
        while ((token = parser.nextToken()) != JsonToken.END_OBJECT) {
            if (token == null) {
                throw new GeneralException("Unexpected end of Davrhub response");
            }

            String fieldName = parser.currentName();
            JsonToken valueToken = parser.nextToken();
            if ("data".equals(fieldName) && valueToken == JsonToken.START_ARRAY) {
                syncedFiles += readFilesArray(parser, loanId, branchId);
            } else {
                parser.skipChildren();
            }
        }
        return syncedFiles;
    }

    private int readFilesArray(JsonParser parser, String loanId, String branchId) throws Exception {
        int syncedFiles = 0;
        JsonToken token;
        while ((token = parser.nextToken()) != JsonToken.END_ARRAY) {
            if (token == null) {
                throw new GeneralException("Unexpected end of Davrhub response");
            }
            if (token != JsonToken.START_OBJECT) {
                parser.skipChildren();
                continue;
            }
            if (readDavrhubFile(parser, loanId, branchId)) {
                syncedFiles++;
            }
        }
        return syncedFiles;
    }

    private boolean readDavrhubFile(JsonParser parser, String loanId, String branchId) throws IOException {
        DavrhubFilePayload file = new DavrhubFilePayload();

        JsonToken token;
        while ((token = parser.nextToken()) != JsonToken.END_OBJECT) {
            if (token == null) {
                throw new GeneralException("Unexpected end of Davrhub file item");
            }

            String fieldName = parser.currentName();
            JsonToken valueToken = parser.nextToken();
            switch (fieldName) {
                case "ID":
                    file.id = readLong(parser, valueToken);
                    break;
                case "FILE_NAME":
                    file.fileName = readString(parser, valueToken);
                    break;
                case "SOURCE":
                    readSource(parser, valueToken, file);
                    break;
                case "DTIME":
                    file.dtime = readString(parser, valueToken);
                    break;
                case "DOC_ID":
                    file.docId = normalizeDocId(readString(parser, valueToken));
                    break;
                case "STATE":
                    file.state = readString(parser, valueToken);
                    break;
                case "YEAR":
                    file.year = readInteger(parser, valueToken);
                    break;
                case "QUAR":
                    file.quar = readInteger(parser, valueToken);
                    break;
                case "PLACE_SRC":
                    file.placeSrc = readInteger(parser, valueToken);
                    break;
                case "COMMENT_TEXT":
                    file.commentText = readString(parser, valueToken);
                    break;
                case "NAME_DOC":
                    file.nameDoc = readString(parser, valueToken);
                    break;
                case "NAME_STATE":
                    file.nameState = readString(parser, valueToken);
                    break;
                case "FILE_URL":
                    file.fileUrl = normalizeFileUrl(readString(parser, valueToken));
                    break;
                default:
                    parser.skipChildren();
                    break;
            }
        }

        return saveDavrhubFile(file, loanId, branchId);
    }

    private void readSource(JsonParser parser, JsonToken valueToken, DavrhubFilePayload file) throws IOException {
        if (valueToken == JsonToken.VALUE_NULL) {
            return;
        }
        if (valueToken != JsonToken.VALUE_STRING) {
            parser.skipChildren();
            return;
        }
        spoolSource(parser, file);
    }

    private void spoolSource(JsonParser parser, DavrhubFilePayload file) throws IOException {
        cleanupSource(file);

        Path tempFile = Files.createTempFile("davrhub-source-", ".bin");
        MessageDigest digest = newSha256Digest();
        CountingOutputStream countingOutput = null;
        try {
            countingOutput = new CountingOutputStream(new DigestOutputStream(Files.newOutputStream(tempFile), digest));
            parser.readBinaryValue(countingOutput);
            countingOutput.close();

            file.fileSize = countingOutput.getByteCount();
            if (file.fileSize == 0) {
                Files.deleteIfExists(tempFile);
                return;
            }
            file.sourceHash = HexFormat.of().formatHex(digest.digest());
            file.sourceTempFile = tempFile;
        } catch (IOException | RuntimeException e) {
            closeQuietly(countingOutput);
            deleteTempFileQuietly(tempFile);
            throw e;
        }
    }

    private boolean saveDavrhubFile(DavrhubFilePayload file, String loanId, String branchId) {
        log.info("DAVRHUB SAVE START loanId={}, branchId={}, docId={}, fileName={}, fileSize={}, hasFileUrl={}, hasSource={}",
                loanId, branchId, file.docId, file.fileName, file.fileSize, file.fileUrl != null, file.sourceTempFile != null);
        boolean externalUrlMode = file.fileUrl != null;
        String objectName = externalUrlMode
                ? buildDavrhubObjectName(sha256Hex(file.fileUrl))
                : buildDavrhubObjectName(file.sourceHash);
        if (objectName == null) {
            log.warn("DAVRHUB SAVE SKIP loanId={}, branchId={}, docId={}, reason=objectName is null (invalid hash)", loanId, branchId, file.docId);
            cleanupSource(file);
            return false;
        }

        try {
            Attachment attachment = attachmentService.get(objectName);
            if (attachment != null && loanAttachmentService.get(loanId, branchId, attachment.getId()) != null) {
                log.info("DAVRHUB SAVE SKIP DUPLICATE loanId={}, branchId={}, objectName={}, attachmentId={}", loanId, branchId, objectName, attachment.getId());
                return true;
            }

            if (attachment == null) {
                if (externalUrlMode) {
                    attachment = attachmentService.create(new AttachmentDTO(
                            objectName,
                            resolveFileName(file.fileName),
                            file.fileUrl,
                            DAVRHUB_SOURCE,
                            resolveDisplayName(file),
                            file.fileSize > 0 ? file.fileSize : null,
                            detectContentType(file)
                    ));
                } else {
                    if (file.sourceTempFile == null || file.fileSize <= 0) {
                        log.warn("DAVRHUB file skipped because SOURCE and FILE_URL are empty docId={}, fileName={}", file.docId, file.fileName);
                        return false;
                    }

                    uploadSpooledSource(file, objectName);
                    attachment = attachmentService.create(new AttachmentDTO(
                            objectName,
                            resolveFileName(file.fileName),
                            null,
                            DAVRHUB_SOURCE,
                            resolveDisplayName(file),
                            file.fileSize,
                            detectContentType(file)
                    ));
                }
            }

            loanAttachmentService.create(new LoanAttachmentDTO(attachment.getId(), branchId, loanId));
            log.info("DAVRHUB SAVE OK loanId={}, branchId={}, objectName={}, attachmentId={}", loanId, branchId, objectName, attachment.getId());
            return true;
        } finally {
            cleanupSource(file);
        }
    }

    private void uploadSpooledSource(DavrhubFilePayload file, String objectName) {
        log.info("DAVRHUB UPLOAD START objectName={}, tempFile={}, fileSize={} bytes", objectName, file.sourceTempFile, file.fileSize);
        long uploadStart = System.currentTimeMillis();
        try (InputStream inputStream = Files.newInputStream(file.sourceTempFile)) {
            fileStorageService.uploadAs(inputStream, file.fileSize, objectName);
            log.info("DAVRHUB UPLOAD OK objectName={}, fileSize={} bytes, duration={}ms", objectName, file.fileSize, System.currentTimeMillis() - uploadStart);
        } catch (IOException e) {
            log.error("DAVRHUB UPLOAD FAILED objectName={}, tempFile={}, fileSize={}, duration={}ms, error={}", objectName, file.sourceTempFile, file.fileSize, System.currentTimeMillis() - uploadStart, e.getMessage());
            throw new GeneralException("Error reading Davrhub temp file", e);
        }
    }

    private String buildDavrhubObjectName(String sourceHash) {
        String normalizedSourceHash = normalizeSourceHash(sourceHash);
        return normalizedSourceHash == null ? null : DAVRHUB_SOURCE + "_" + normalizedSourceHash;
    }

    private String resolveDisplayName(DavrhubFilePayload file) {
        if (file.nameDoc != null && !file.nameDoc.isBlank()) {
            return file.nameDoc;
        }
        return resolveFileName(file.fileName);
    }

    private String resolveFileName(String fileName) {
        return fileName == null || fileName.isBlank() ? DEFAULT_FILE_NAME : fileName;
    }

    private String detectContentType(DavrhubFilePayload file) {
        if (file.sourceTempFile == null) {
            String nameDetected = URLConnection.guessContentTypeFromName(resolveFileName(file.fileName));
            return nameDetected == null || nameDetected.isBlank() ? DEFAULT_CONTENT_TYPE : nameDetected;
        }
        try (InputStream inputStream = Files.newInputStream(file.sourceTempFile)) {
            String contentType = TIKA.detect(inputStream, resolveFileName(file.fileName));
            return contentType == null || contentType.isBlank() ? DEFAULT_CONTENT_TYPE : contentType;
        } catch (IOException e) {
            log.warn("Failed to detect Davrhub content type docId={}, fileName={}", file.docId, file.fileName, e);
            return DEFAULT_CONTENT_TYPE;
        }
    }

    private void cleanupSource(DavrhubFilePayload file) {
        if (file == null) {
            return;
        }
        deleteTempFileQuietly(file.sourceTempFile);
        file.sourceTempFile = null;
        file.fileSize = 0;
        file.sourceHash = null;
    }

    private void deleteTempFileQuietly(Path tempFile) {
        if (tempFile == null) {
            return;
        }
        try {
            Files.deleteIfExists(tempFile);
        } catch (IOException e) {
            log.warn("Failed to delete Davrhub temp file {}", tempFile, e);
        }
    }

    private void closeQuietly(AutoCloseable closeable) {
        if (closeable == null) {
            return;
        }
        try {
            closeable.close();
        } catch (Exception e) {
            log.warn("Failed to close Davrhub stream", e);
        }
    }

    private String normalizeDocId(String docId) {
        if (docId == null) {
            return null;
        }
        String normalized = docId.trim();
        return normalized.isBlank() ? null : normalized;
    }

    private String normalizeFileUrl(String fileUrl) {
        if (fileUrl == null) {
            return null;
        }
        String normalized = fileUrl.trim();
        return normalized.isBlank() ? null : normalized;
    }

    private String sha256Hex(String value) {
        return HexFormat.of().formatHex(newSha256Digest().digest(value.getBytes(StandardCharsets.UTF_8)));
    }

    private String normalizeSourceHash(String sourceHash) {
        if (sourceHash == null) {
            return null;
        }
        String normalized = sourceHash.trim();
        return normalized.matches("[0-9a-f]{64}") ? normalized : null;
    }

    private MessageDigest newSha256Digest() {
        try {
            return MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new GeneralException("SHA-256 algorithm not found", e);
        }
    }

    private String readString(JsonParser parser, JsonToken token) throws IOException {
        if (token == JsonToken.VALUE_NULL) {
            return null;
        }
        if (token.isScalarValue()) {
            return parser.getValueAsString();
        }
        parser.skipChildren();
        return null;
    }

    private Long readLong(JsonParser parser, JsonToken token) throws IOException {
        if (token == JsonToken.VALUE_NULL) {
            return null;
        }
        if (token.isNumeric()) {
            return parser.getLongValue();
        }

        String value = readString(parser, token);
        if (value == null || value.isBlank()) {
            return null;
        }
        return Long.valueOf(value);
    }

    private Integer readInteger(JsonParser parser, JsonToken token) throws IOException {
        if (token == JsonToken.VALUE_NULL) {
            return null;
        }
        if (token.isNumeric()) {
            return parser.getIntValue();
        }

        String value = readString(parser, token);
        if (value == null || value.isBlank()) {
            return null;
        }
        return Integer.valueOf(value);
    }

    public long getSyncDelaySeconds() {
        return syncDelayMillis / 1000L;
    }

    public long updateSyncDelaySeconds(long seconds) {
        if (seconds < 0) {
            throw new GeneralException("Sync delay seconds must not be negative");
        }
        syncDelayMillis = seconds * 1000L;
        log.info("SYNC delay updated to {} seconds ({} ms)", seconds, syncDelayMillis);
        return getSyncDelaySeconds();
    }

    public void deleteFileUploads() {
        loanAttachmentRepository.deleteAll();
    }

    private static final class DavrhubFilePayload {
        private Long id;
        private String fileName;
        private String dtime;
        private String docId;
        private String state;
        private Integer year;
        private Integer quar;
        private Integer placeSrc;
        private String commentText;
        private String nameDoc;
        private String nameState;
        private String fileUrl;
        private Path sourceTempFile;
        private long fileSize;
        private String sourceHash;
    }

    private static final class CountingOutputStream extends OutputStream {
        private final OutputStream delegate;
        private long byteCount;

        private CountingOutputStream(OutputStream delegate) {
            this.delegate = delegate;
        }

        @Override
        public void write(int b) throws IOException {
            delegate.write(b);
            byteCount++;
        }

        @Override
        public void write(byte[] b, int off, int len) throws IOException {
            delegate.write(b, off, len);
            byteCount += len;
        }

        @Override
        public void flush() throws IOException {
            delegate.flush();
        }

        @Override
        public void close() throws IOException {
            delegate.close();
        }

        private long getByteCount() {
            return byteCount;
        }
    }
}

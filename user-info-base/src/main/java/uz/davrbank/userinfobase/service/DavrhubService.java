package uz.davrbank.userinfobase.service;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import reactor.core.publisher.Flux;
import uz.davrbank.userinfobase.client.DavrhubGatewayClient;
import uz.davrbank.userinfobase.dto.DavrhubDtoResponse;
import uz.davrbank.userinfobase.dto.request.DavrhubFileListRequest;
import uz.davrbank.userinfobase.entity.DavrhubFile;
import uz.davrbank.userinfobase.entity.ExternalFile;
import uz.davrbank.userinfobase.enums.SystemSource;
import uz.davrbank.userinfobase.exceptions.GeneralException;
import uz.davrbank.userinfobase.repository.DavrhubFileRepository;
import uz.davrbank.userinfobase.repository.ExternalFileRepository;

import java.io.*;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DavrhubService {

    private static final String DAVRHUB_FILE_LIST_URI = "/api/v1/davrhub/execute/list/421";
    private static final int STREAM_BUFFER_SIZE = 32;
    private static final int BASE64_PIPE_BUFFER_SIZE = 4 * 1024 * 1024;
    private static final String DEFAULT_FILE_NAME = "davrhub-file";

    private final DavrhubFileRepository repository;
    private final ExternalFileRepository externalFileRepository;
    private final FileStorageService fileStorageService;
    private final HashIdService hashIdService;
    private final DavrhubGatewayClient davrhubGatewayClient;
    @Qualifier("fileExecutor")
    private final Executor fileExecutor;
    private final ObjectMapper objectMapper;
    private final TransactionTemplate transactionTemplate;

    public DavrhubService(DavrhubFileRepository repository, ExternalFileRepository externalFileRepository,
                          FileStorageService fileStorageService, HashIdService hashIdService,
                          DavrhubGatewayClient davrhubGatewayClient, @Qualifier("fileExecutor") Executor fileExecutor,
                          ObjectMapper objectMapper, TransactionTemplate transactionTemplate
    ) {
        this.repository = repository;
        this.externalFileRepository = externalFileRepository;
        this.fileStorageService = fileStorageService;
        this.hashIdService = hashIdService;
        this.davrhubGatewayClient = davrhubGatewayClient;
        this.fileExecutor = fileExecutor;
        this.objectMapper = objectMapper;
        this.transactionTemplate = transactionTemplate;
    }

    private Long parseTransId(DavrhubFileListRequest request) {
        if (request == null || request.pTransId() == null || request.pTransId().isBlank()) {
            throw new GeneralException("P_TRANS_ID is required");
        }
        try {
            return Long.valueOf(request.pTransId().trim());
        } catch (NumberFormatException e) {
            throw new GeneralException("P_TRANS_ID must contain only digits", e);
        }
    }

    public List<DavrhubDtoResponse> getFiles(DavrhubFileListRequest request) {
        long methodStartedAt = System.nanoTime();
        Long transId = parseTransId(request);
        log.info("DAVRHUB SYNC START transId={}", transId);

        DavrhubSyncSnapshot snapshot = synchronizeFiles(request, transId);
        List<DavrhubFile> files = repository.findByTransId(transId);
        logSyncSummary("getFiles", methodStartedAt, snapshot, transId, files.size());

        return files.stream()
                .map(file -> DavrhubDtoResponse.fromEntity(file, hashIdService))
                .toList();
    }

    public void syncFiles(DavrhubFileListRequest request) {
        long methodStartedAt = System.nanoTime();
        Long transId = parseTransId(request);
        log.info("DAVRHUB SYNC START transId={}", transId);

        DavrhubSyncSnapshot snapshot = synchronizeFiles(request, transId);
        long totalFiles = repository.countByTransId(transId);
        logSyncSummary("syncFiles", methodStartedAt, snapshot, transId, totalFiles);
    }

    public int backfillExternalFiles(DavrhubFileListRequest request) {
        Long transId = parseTransId(request);
        List<DavrhubFile> davrhubFiles = repository.findByTransId(transId);
        Set<String> docIds = davrhubFiles.stream()
                .map(DavrhubFile::getDocId)
                .map(this::normalizeDocId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        if (docIds.isEmpty()) {
            return 0;
        }

        Set<String> persistedExternalDocIds = externalFileRepository
                .findExternalFileIdsBySourceAndExternalFileIdIn(SystemSource.NCI, docIds).stream()
                .map(this::normalizeDocId)
                .filter(Objects::nonNull)
                .collect(Collectors.toCollection(HashSet::new));

        List<ExternalFile> missingExternalFiles = new ArrayList<>();
        for (DavrhubFile davrhubFile : davrhubFiles) {
            String docId = normalizeDocId(davrhubFile.getDocId());
            if (docId == null || !persistedExternalDocIds.add(docId)) {
                continue;
            }
            missingExternalFiles.add(toExternalFile(davrhubFile, request));
        }

        if (missingExternalFiles.isEmpty()) {
            return 0;
        }

        externalFileRepository.saveAll(missingExternalFiles);
        log.info(
                "DAVRHUB EXTERNAL BACKFILL transId={}, loanId={}, backfilledFiles={}",
                transId,
                request.pTransId(),
                missingExternalFiles.size()
        );
        return missingExternalFiles.size();
    }

    public List<DavrhubFile> getPersistedFiles(String loanId) {
        return repository.findByTransId(Long.valueOf(loanId));
    }

    private DavrhubSyncSnapshot synchronizeFiles(DavrhubFileListRequest request, Long transId) {
        Set<String> knownDocIds = loadKnownDocIds(transId, request.pTransId());
        long processingStartedAt = System.nanoTime();
        List<PreparedDavrhubEntities> preparedFiles = streamDavrhubFiles(request, transId, knownDocIds);
        int savedCount = savePreparedFiles(transId, preparedFiles);
        return new DavrhubSyncSnapshot(processingStartedAt, preparedFiles.size(), savedCount);
    }

    private void logSyncSummary(
            String operationName,
            long methodStartedAt,
            DavrhubSyncSnapshot snapshot,
            Long transId,
            long totalFiles
    ) {
        log.info(
                "Davrhub processed in {} milliseconds: transId={}, preparedFiles={}, newDavrhubFiles={}, totalFiles={}",
                elapsedMillis(snapshot.processingStartedAt()),
                transId,
                snapshot.preparedFiles(),
                snapshot.newFiles(),
                totalFiles
        );
        log.info(
                "{} method takes {} milliseconds: transId={}, totalFiles={}, newFiles={}",
                operationName,
                elapsedMillis(methodStartedAt),
                transId,
                totalFiles,
                snapshot.newFiles()
        );
        log.info("DAVRHUB SYNC END transId={}, totalFiles={}, newFiles={}", transId, totalFiles, snapshot.newFiles());
    }

    private Set<String> loadKnownDocIds(Long transId, String loanId) {
        Set<String> davrhubDocIds = repository.findDocIdsByTransId(transId).stream()
                .map(this::normalizeDocId)
                .filter(Objects::nonNull)
                .collect(Collectors.toCollection(HashSet::new));

        externalFileRepository.findExternalFileIdsByLoanIdAndSource(loanId, SystemSource.NCI).stream()
                .map(this::normalizeDocId)
                .filter(Objects::nonNull)
                .forEach(davrhubDocIds::add);

        return davrhubDocIds;
    }

    private List<PreparedDavrhubEntities> streamDavrhubFiles(
            DavrhubFileListRequest request,
            Long transId,
            Set<String> knownDocIds
    ) {
        Flux<DataBuffer> responseBody = davrhubGatewayClient.stream(
                HttpMethod.POST,
                DAVRHUB_FILE_LIST_URI,
                request
        );

        List<PreparedDavrhubEntities> preparedFiles = new ArrayList<>();
        try {
            try (InputStream responseStream = toInputStream(responseBody);
                 JsonParser parser = objectMapper.getFactory().createParser(responseStream)) {
                parseDavrhubResponse(parser, request, transId, knownDocIds, preparedFiles);
            }
        } catch (Exception e) {
            throw new GeneralException("Error fetching files from Davrhub", e);
        }
        return preparedFiles;
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

    private void parseDavrhubResponse(
            JsonParser parser,
            DavrhubFileListRequest request,
            Long transId,
            Set<String> knownDocIds,
            List<PreparedDavrhubEntities> preparedFiles
    ) throws Exception {
        JsonToken firstToken = parser.nextToken();

        if (firstToken == null) {
            return;
        }

        if (firstToken == JsonToken.START_ARRAY) {
            readFilesArray(parser, request, transId, knownDocIds, preparedFiles);
            return;
        }

        if (firstToken != JsonToken.START_OBJECT) {
            throw new GeneralException("Unknown Davrhub response format");
        }

        JsonToken token;
        while ((token = parser.nextToken()) != JsonToken.END_OBJECT) {
            if (token == null) {
                throw new GeneralException("Unexpected end of Davrhub response");
            }
            String fieldName = parser.currentName();
            JsonToken valueToken = parser.nextToken();

            if ("data".equals(fieldName) && valueToken == JsonToken.START_ARRAY) {
                readFilesArray(parser, request, transId, knownDocIds, preparedFiles);
            } else {
                parser.skipChildren();
            }
        }
    }

    private void readFilesArray(
            JsonParser parser,
            DavrhubFileListRequest request,
            Long transId,
            Set<String> knownDocIds,
            List<PreparedDavrhubEntities> preparedFiles
    ) throws Exception {
        JsonToken token;
        while ((token = parser.nextToken()) != JsonToken.END_ARRAY) {
            if (token == null) {
                throw new GeneralException("Unexpected end of Davrhub response");
            }
            if (token != JsonToken.START_OBJECT) {
                parser.skipChildren();
                continue;
            }
            readDavrhubFile(parser, request, transId, knownDocIds)
                    .ifPresent(preparedFiles::add);
        }
    }

    private Optional<PreparedDavrhubEntities> readDavrhubFile(
            JsonParser parser,
            DavrhubFileListRequest request,
            Long transId,
            Set<String> knownDocIds
    ) throws IOException {
        DavrhubFilePayload file = new DavrhubFilePayload();
        boolean shouldStore = true;

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
                    if (shouldStore) {
                        readSource(parser, valueToken, file);
                    } else {
                        parser.skipChildren();
                    }
                    break;
                case "DTIME":
                    file.dtime = readString(parser, valueToken);
                    break;
                case "DOC_ID":
                    file.docId = normalizeDocId(readString(parser, valueToken));
                    shouldStore = file.docId != null && knownDocIds.add(file.docId);
                    if (!shouldStore) {
                        cleanupSource(file);
                    }
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
                default:
                    parser.skipChildren();
                    break;
            }
        }

        if (!shouldStore || file.docId == null) {
            cleanupSource(file);
            return Optional.empty();
        }

        uploadSpooledSource(file);
        return Optional.of(prepareEntities(file, request, transId));
    }

    private PreparedDavrhubEntities prepareEntities(
            DavrhubFilePayload response,
            DavrhubFileListRequest request,
            Long transId
    ) {
        long fileSize = response.fileSize;
        String mimeType = getMimeType(URLConnection.guessContentTypeFromName(response.fileName));

        DavrhubFile davrhubFile = DavrhubFile.builder()
                .transId(transId)
                .davrhubId(response.id)
                .fileName(response.fileName)
                .dtime(response.dtime)
                .docId(response.docId)
                .state(response.state)
                .year(response.year)
                .quar(response.quar)
                .placeSrc(response.placeSrc)
                .commentText(response.commentText)
                .nameDoc(response.nameDoc)
                .nameState(response.nameState)
                .minioObjectName(response.minioObjectName)
                .fileSize(fileSize)
                .build();

        ExternalFile externalFile = ExternalFile.builder()
                .source(SystemSource.NCI)
                .loanId(request.pTransId())
                .branchId(request.pBranch())
                .fileName(response.fileName)
                .nameDoc(response.nameDoc)
                .externalFileId(response.docId)
                .fileSize(fileSize)
                .minioObjectName(response.minioObjectName)
                .mimeType(mimeType)
                .build();

        log.info("PREPARED {} size={} bytes", response.fileName, fileSize);
        return new PreparedDavrhubEntities(davrhubFile, externalFile);
    }

    private ExternalFile toExternalFile(DavrhubFile davrhubFile, DavrhubFileListRequest request) {
        return ExternalFile.builder()
                .source(SystemSource.NCI)
                .loanId(request.pTransId())
                .branchId(request.pBranch())
                .fileName(davrhubFile.getFileName())
                .nameDoc(davrhubFile.getNameDoc())
                .externalFileId(normalizeDocId(davrhubFile.getDocId()))
                .fileSize(davrhubFile.getFileSize())
                .minioObjectName(davrhubFile.getMinioObjectName())
                .mimeType(getMimeType(URLConnection.guessContentTypeFromName(davrhubFile.getFileName())))
                .build();
    }

    private void readSource(
            JsonParser parser,
            JsonToken valueToken,
            DavrhubFilePayload file
    ) throws IOException {
        if (valueToken == JsonToken.VALUE_NULL) {
            return;
        }
        if (valueToken != JsonToken.VALUE_STRING) {
            parser.skipChildren();
            return;
        }

        if (file.docId == null) {
            spoolSource(parser, file);
            return;
        }

        UploadedSource source = uploadSourceToMinio(parser, file.fileName);
        file.minioObjectName = source.objectName();
        file.fileSize = source.fileSize();
    }

    private void spoolSource(JsonParser parser, DavrhubFilePayload file) throws IOException {
        Path tempFile = Files.createTempFile("davrhub-source-", ".bin");
        CountingOutputStream countingOutput = null;
        try {
            countingOutput = new CountingOutputStream(Files.newOutputStream(tempFile));
            parser.readBinaryValue(countingOutput);
            countingOutput.close();

            file.fileSize = countingOutput.getByteCount();
            if (file.fileSize == 0) {
                Files.deleteIfExists(tempFile);
                return;
            }
            file.sourceTempFile = tempFile;
        } catch (IOException | RuntimeException e) {
            closeQuietly(countingOutput);
            deleteTempFileQuietly(tempFile);
            throw e;
        }
    }

    private void uploadSpooledSource(DavrhubFilePayload file) {
        if (file.sourceTempFile == null) {
            return;
        }

        try (InputStream inputStream = Files.newInputStream(file.sourceTempFile)) {
            file.minioObjectName = fileStorageService.upload(
                    inputStream,
                    file.fileSize,
                    resolveFileName(file.fileName)
            );
        } catch (IOException e) {
            throw new GeneralException("Error reading Davrhub temp file", e);
        } finally {
            deleteSpooledSource(file);
        }
    }

    private UploadedSource uploadSourceToMinio(JsonParser parser, String fileName) throws IOException {
        PipedInputStream uploadInput = new PipedInputStream(BASE64_PIPE_BUFFER_SIZE);
        PipedOutputStream parserOutput = new PipedOutputStream(uploadInput);

        CompletableFuture<String> uploadTask = CompletableFuture.supplyAsync(() -> {
            try (InputStream inputStream = uploadInput) {
                return fileStorageService.uploadUnknownSize(inputStream, resolveFileName(fileName));
            } catch (Exception e) {
                throw new CompletionException(e);
            }
        }, fileExecutor);

        CountingOutputStream countingOutput = new CountingOutputStream(parserOutput);
        try (countingOutput) {
            parser.readBinaryValue(countingOutput);
        } catch (IOException | RuntimeException e) {
            closeQuietly(uploadInput);
            uploadTask.cancel(true);
            throw e;
        }

        String objectName = awaitUpload(uploadTask);
        long fileSize = countingOutput.getByteCount();
        if (fileSize == 0) {
            deleteQuietly(objectName);
            objectName = null;
        }
        return new UploadedSource(objectName, fileSize);
    }

    private String awaitUpload(CompletableFuture<String> uploadTask) {
        try {
            return uploadTask.join();
        } catch (CompletionException e) {
            Throwable cause = e.getCause();
            if (cause instanceof RuntimeException runtimeException) {
                throw runtimeException;
            }
            log.error("Error file upload: {}", e.getMessage());
            throw new GeneralException("Error uploading file to MinIO", e);
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

    private int savePreparedFiles(Long transId, List<PreparedDavrhubEntities> preparedFiles) {
        if (preparedFiles.isEmpty()) {
            return 0;
        }

        Set<String> docIds = preparedFiles.stream()
                .map(PreparedDavrhubEntities::docId)
                .collect(Collectors.toSet());

        Set<String> persistedDavrhubDocIds = repository.findDocIdsByTransIdAndDocIdIn(transId, docIds).stream()
                .map(this::normalizeDocId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        Set<String> persistedExternalDocIds = externalFileRepository
                .findExternalFileIdsBySourceAndExternalFileIdIn(SystemSource.NCI, docIds).stream()
                .map(this::normalizeDocId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        List<PreparedDavrhubEntities> davrhubFilesToSave = preparedFiles.stream()
                .filter(file -> !persistedDavrhubDocIds.contains(file.docId()))
                .toList();

        List<PreparedDavrhubEntities> externalFilesToSave = preparedFiles.stream()
                .filter(file -> !persistedExternalDocIds.contains(file.docId()))
                .toList();

        Set<String> retainedDocIds = new HashSet<>();
        davrhubFilesToSave.stream()
                .map(PreparedDavrhubEntities::docId)
                .forEach(retainedDocIds::add);
        externalFilesToSave.stream()
                .map(PreparedDavrhubEntities::docId)
                .forEach(retainedDocIds::add);

        preparedFiles.stream()
                .filter(file -> !retainedDocIds.contains(file.docId()))
                .map(PreparedDavrhubEntities::minioObjectName)
                .filter(Objects::nonNull)
                .forEach(this::deleteQuietly);

        if (davrhubFilesToSave.isEmpty() && externalFilesToSave.isEmpty()) {
            return 0;
        }

        transactionTemplate.executeWithoutResult(status -> {
            if (!davrhubFilesToSave.isEmpty()) {
                repository.saveAll(davrhubFilesToSave.stream()
                        .map(PreparedDavrhubEntities::davrhubFile)
                        .toList());
            }

            if (!externalFilesToSave.isEmpty()) {
                externalFileRepository.saveAll(externalFilesToSave.stream()
                        .map(PreparedDavrhubEntities::externalFile)
                        .toList());
            }
        });
        return davrhubFilesToSave.size();
    }

    private void deleteQuietly(String objectName) {
        if (objectName == null) {
            return;
        }
        try {
            fileStorageService.delete(objectName);
        } catch (Exception e) {
            log.warn("Failed to cleanup unused MinIO object {}", objectName, e);
        }
    }

    private void deleteUploadedFile(DavrhubFilePayload file) {
        if (file.minioObjectName != null) {
            deleteQuietly(file.minioObjectName);
            file.minioObjectName = null;
            file.fileSize = 0;
        }
    }

    private void cleanupSource(DavrhubFilePayload file) {
        deleteUploadedFile(file);
        deleteSpooledSource(file);
        file.fileSize = 0;
    }

    private void deleteSpooledSource(DavrhubFilePayload file) {
        if (file.sourceTempFile != null) {
            deleteTempFileQuietly(file.sourceTempFile);
            file.sourceTempFile = null;
        }
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

    private String resolveFileName(String fileName) {
        return fileName == null || fileName.isBlank() ? DEFAULT_FILE_NAME : fileName;
    }

    private long elapsedMillis(long startedAtNanos) {
        return TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startedAtNanos);
    }

    private record UploadedSource(String objectName, long fileSize) {
    }

    private record DavrhubSyncSnapshot(long processingStartedAt, int preparedFiles, int newFiles) {
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
        private String minioObjectName;
        private Path sourceTempFile;
        private long fileSize;
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

    private record PreparedDavrhubEntities(
            DavrhubFile davrhubFile,
            ExternalFile externalFile
    ) {
        private String docId() {
            return externalFile.getExternalFileId();
        }

        private String minioObjectName() {
            return davrhubFile.getMinioObjectName();
        }
    }

    @Transactional(readOnly = true)
    public ResponseEntity<StreamingResponseBody> downloadFile(Long id) {

        DavrhubFile file = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("File not found"));

        if (file.getMinioObjectName() == null) {
            throw new GeneralException("File not stored in MinIO");
        }

        StreamingResponseBody body = outputStream -> {
            try (InputStream inputStream = fileStorageService.openDownloadStream(file.getMinioObjectName())) {
                inputStream.transferTo(outputStream);
            }
        };

        String mimeType = getMimeType(URLConnection.guessContentTypeFromName(file.getFileName()));
        ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(mimeType))
                .header(HttpHeaders.CONTENT_DISPOSITION, attachmentHeader(file.getFileName()));

        if (file.getFileSize() != null && file.getFileSize() > 0) {
            responseBuilder.contentLength(file.getFileSize());
        }

        return responseBuilder.body(body);
    }


    @Transactional
    public void deleteFile(Long id) {
        DavrhubFile file = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("File not found"));
        fileStorageService.delete(file.getMinioObjectName());
        repository.delete(file);
    }


    @NotNull
    public static String getMimeType(String mimeType) {
        if (mimeType == null) {
            mimeType = "application/octet-stream";
        }
        return mimeType;
    }

    private String attachmentHeader(String fileName) {
        return ContentDisposition.attachment()
                .filename(resolveFileName(fileName), StandardCharsets.UTF_8)
                .build()
                .toString();
    }
}

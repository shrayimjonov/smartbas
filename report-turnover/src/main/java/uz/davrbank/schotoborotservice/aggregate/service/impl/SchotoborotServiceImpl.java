package uz.davrbank.schotoborotservice.aggregate.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.RetryableException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;
import uz.davrbank.schotoborotservice.aggregate.dto.integration.base.PageableIntegrationResponseDTO;
import uz.davrbank.schotoborotservice.aggregate.dto.integration.request.SchotoborotGetPageableRequest;
import uz.davrbank.schotoborotservice.aggregate.dto.integration.response.SchotoborotGetPageableResponse;
import uz.davrbank.schotoborotservice.aggregate.filter.SchotoborotFilter;
import uz.davrbank.schotoborotservice.aggregate.integration.SchotoborotHubClient;
import uz.davrbank.schotoborotservice.aggregate.service.MinioService;
import uz.davrbank.schotoborotservice.aggregate.service.SchotoborotCsvFileGenerator;
import uz.davrbank.schotoborotservice.aggregate.service.SchotoborotFileGenerator;
import uz.davrbank.schotoborotservice.aggregate.service.SchotoborotService;
import uz.davrbank.schotoborotservice.exception.condition.BadRequestException;
import uz.davrbank.schotoborotservice.exception.condition.CustomInternalServerException;
import uz.davrbank.schotoborotservice.exception.condition.NotFoundException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketTimeoutException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeoutException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class SchotoborotServiceImpl implements SchotoborotService {

    SchotoborotHubClient hubClient;
    SchotoborotFileGenerator fileGenerator;
    SchotoborotCsvFileGenerator csvFileGenerator;
    MinioService minioService;
    ObjectMapper objectMapper;
    static final int MAX_PAGE_SIZE = 200_000;
    static final String SERVICE_NAME = "oborot";

    @Override
    public ResponseEntity<PageableIntegrationResponseDTO<SchotoborotGetPageableResponse>> schotoborotGetPageable(SchotoborotFilter filter, String username) {
        log.info("Get all pagination.  username: {}, startDate: {}, endDate: {}, account: {}, page: {}, size: {} ", username, filter.getStartDate(), filter.getEndDate(), filter.getAccount(), filter.getPageNumber(), filter.getPageSize());
        validateFilter(filter);
        PageableIntegrationResponseDTO<SchotoborotGetPageableResponse> response;
        try {
            SchotoborotGetPageableRequest request = new SchotoborotGetPageableRequest();
            request.setIStartDate(filter.getStartDate());
            request.setIEndDate(filter.getEndDate());
            request.setIAccount(filter.getAccount());
            request.setIPageNumber(Optional.ofNullable(filter.getPageNumber()).orElse(1));
            request.setIPageSize(Optional.ofNullable(filter.getPageSize()).orElse(20));
            log.info("Request: {}", objectMapper.writeValueAsString(request));
            response = hubClient.getAll(request);
            response.setStatus("success");
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
            throw new RuntimeException("Error: " + e.getMessage());
        }
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<ByteArrayResource> schotoborotDownloadExcel(SchotoborotFilter filter, String username) {

        log.info("Generating Excel report.  username: {}, startDate: {}, endDate: {}, account: {}", username, filter.getStartDate(), filter.getEndDate(), filter.getAccount());

        validateFilter(filter);

        List<SchotoborotGetPageableResponse> result = getAll(filter);
        if (CollectionUtils.isEmpty(result)) {
            throw new NotFoundException("No data found for the given filter");
        }
        byte[] content;
        try {
            content = fileGenerator.generate(result, username);
        } catch (Exception e) {
            log.error("Failed to generate report file: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to generate report file");
        }
        String url;
        try {
            url = minioService.createExcel(filter, content);
        } catch (Exception e) {
            log.error("Failed to upload Excel to MinIO: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to upload Excel to MinIO: " + e.getMessage());
        }

        return buildExcelResponse(content, url);
    }

    @Override
    public ResponseEntity<ByteArrayResource> schotoborotDownloadCsv(SchotoborotFilter filter, String username) {
        log.info("Generating CSV report. username: {}, startDate: {}, endDate: {}, account: {}", username, filter.getStartDate(), filter.getEndDate(), filter.getAccount());

        validateFilter(filter);

        String safeUsername = StringUtils.hasText(username) ? username.trim() : "anonymous";
        String folderName = buildFolderName(safeUsername, filter);
        long currentTime=System.currentTimeMillis();
        log.info("Generating started. folderName: {}", folderName);
        boolean useCache = shouldCheckCache(filter);

        if (useCache) {
            Optional<byte[]> cached = minioService.getCsvZipIfExists(folderName);
            if (cached.isPresent()) {
                return buildCsvZipResponse(cached.get(), folderName + ".zip");
            }
        }

        try {
            minioService.deleteCsvZipIfExists(folderName);
            minioService.deleteCsvObjects(folderName);
            int totalFetched = generateCsvFilesToMinio(filter, folderName);
            if (totalFetched == 0) {
                throw new NotFoundException("No data found for the given filter");
            }
            byte[] zipBytes = zipCsvObjectsFromMinio(folderName);
            minioService.createCsvZip(folderName, zipBytes);
            try {
                minioService.deleteCsvObjects(folderName);
            } catch (RuntimeException e) {
                log.warn("Failed to delete CSV objects after zipping. folderName: {}", folderName, e);
            }
            log.info("Generating finished in {} ms. folderName: {}", (System.currentTimeMillis()-currentTime)/1_000, folderName);
            return buildCsvZipResponse(zipBytes, folderName + ".zip");
        } catch (IOException e) {
            log.error("Failed to generate CSV report: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to generate CSV report", e);
        }
    }

    @Override
    public ResponseEntity<ByteArrayResource> schotoborotDownloadExcelZip(SchotoborotFilter filter, String username) {
        log.info("Generating Excel zip report. username: {}, startDate: {}, endDate: {}, account: {}", username, filter.getStartDate(), filter.getEndDate(), filter.getAccount());

        validateFilter(filter);

        String safeUsername = StringUtils.hasText(username) ? username.trim() : "anonymous";
        String folderName = buildFolderName(safeUsername, filter);
        boolean useCache = shouldCheckCache(filter);

        if (useCache) {
            Optional<byte[]> cached = minioService.getExcelZipIfExists(folderName);
            if (cached.isPresent()) {
                return buildExcelZipResponse(cached.get(), folderName + ".zip");
            }
        }

        try {
            minioService.deleteExcelZipIfExists(folderName);
            minioService.deleteExcelObjects(folderName);
            int totalFetched = generateExcelFilesToMinio(filter, username, folderName);
            if (totalFetched == 0) {
                throw new NotFoundException("No data found for the given filter");
            }
            byte[] zipBytes = zipExcelObjectsFromMinio(folderName);
            minioService.createExcelZip(folderName, zipBytes);
            try {
                minioService.deleteExcelObjects(folderName);
            } catch (RuntimeException e) {
                log.warn("Failed to delete Excel objects after zipping. folderName: {}", folderName, e);
            }
            return buildExcelZipResponse(zipBytes, folderName + ".zip");
        } catch (IOException e) {
            log.error("Failed to generate Excel zip report: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to generate Excel zip report", e);
        }
    }


    private List<SchotoborotGetPageableResponse> getAll(SchotoborotFilter filter) {
        List<SchotoborotGetPageableResponse> result = new ArrayList<>();
        int page = 1;
        int totalFetched = 0;
        final int MAX_PAGE_LIMIT = 1000;

        while (true) {
            PageableIntegrationResponseDTO<SchotoborotGetPageableResponse> response;
            try {
                SchotoborotGetPageableRequest request = new SchotoborotGetPageableRequest();
                request.setIStartDate(filter.getStartDate());
                request.setIEndDate(filter.getEndDate());
                request.setIAccount(filter.getAccount());
                request.setIPageNumber(page);
                request.setIPageSize(MAX_PAGE_SIZE);
                response = hubClient.getAll(request);
            } catch (Exception e) {
                log.error("Error: {}", e.getMessage());
                throw new RuntimeException("Error: " + e.getMessage());
            }

            if (response.getData() == null || response.getData().isEmpty()) {
                log.warn("No data returned for page {}", page);
                break;
            }
            result.addAll(response.getData());
            totalFetched += response.getData().size();
            if (page >= MAX_PAGE_LIMIT) {
                log.warn("Max page limit reached");
                break;
            }
            page++;

        }
        log.info("Fetched {} records", totalFetched);
        return result;
    }

    private int generateCsvFilesToMinio(SchotoborotFilter filter, String folderName) throws IOException {
        int page = 1;
        int totalFetched = 0;
        final int maxPageLimit = 1000;
        log.info("Getting data for csv report");
        while (true) {
            log.info("Page: {}, account: {}, startDate: {}, endDate: {}", page, filter.getAccount(), filter.getStartDate(), filter.getEndDate());
            PageableIntegrationResponseDTO<SchotoborotGetPageableResponse> response = fetchPage(filter, page, MAX_PAGE_SIZE);
            List<SchotoborotGetPageableResponse> data = response.getData();
            if (CollectionUtils.isEmpty(data)) {
                if (page == 1) {
                    return 0;
                }
                break;
            }
            String fileName = formatPageFileName("csv", page);
            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                csvFileGenerator.writeCsv(outputStream, data);
                minioService.uploadCsvObject(folderName, fileName, outputStream.toByteArray());
            }
            totalFetched += data.size();
            if (page >= maxPageLimit) {
                log.warn("Max page limit reached for CSV export");
                break;
            }
            page++;
        }

        log.info("Fetched {} records for CSV export", totalFetched);
        return totalFetched;
    }


    private int generateExcelFilesToMinio(SchotoborotFilter filter, String username, String folderName) throws IOException {
        int page = 1;
        int totalFetched = 0;
        final int maxPageLimit = 1000;

        log.info("Getting data for excel report");
        while (true) {
            log.info("Page: {}, account: {}, startDate: {}, endDate: {}", page, filter.getAccount(), filter.getStartDate(), filter.getEndDate());

            PageableIntegrationResponseDTO<SchotoborotGetPageableResponse> response = fetchPage(filter, page, MAX_PAGE_SIZE);
            List<SchotoborotGetPageableResponse> data = response.getData();
            if (CollectionUtils.isEmpty(data)) {
                if (page == 1) {
                    return 0;
                }
                break;
            }
            String fileName = formatPageFileName("xlsx", page);
            byte[] excelBytes = fileGenerator.generate(data, username);
            minioService.uploadExcelObject(folderName, fileName, excelBytes);
            totalFetched += data.size();
            if (page >= maxPageLimit) {
                log.warn("Max page limit reached for Excel export");
                break;
            }
            page++;
        }

        log.info("Fetched {} records for Excel export", totalFetched);
        return totalFetched;
    }

    private PageableIntegrationResponseDTO<SchotoborotGetPageableResponse> fetchPage(SchotoborotFilter filter, int pageNumber, int pageSize) {
        try {
            SchotoborotGetPageableRequest request = new SchotoborotGetPageableRequest();
            request.setIStartDate(filter.getStartDate());
            request.setIEndDate(filter.getEndDate());
            request.setIAccount(filter.getAccount());
            request.setIPageNumber(pageNumber);
            request.setIPageSize(pageSize);
            ResponseEntity<PageableIntegrationResponseDTO<SchotoborotGetPageableResponse>> response = hubClient.getAllExcel(request);
            if (response == null || !response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
                log.error("Error: {}", objectMapper.writeValueAsString(response));
                throw new RuntimeException("Error: " + "No data returned for page " + pageNumber);
            }
            return response.getBody();
        } catch (ResponseStatusException e) {
            log.error("Hub returned non-success status for page {}. status={} reason={}", pageNumber, e.getStatusCode(), e.getReason(), e);
            throw e;
        } catch (RetryableException e) {
            log.error("Timeout while calling hub for page {}", pageNumber, e);
            throw new CustomInternalServerException(buildHubExceptionMessage(e, pageNumber));
        } catch (Exception e) {
            if (isTimeoutException(e)) {
                log.error("Timeout while calling hub for page {}", pageNumber, e);
                throw new CustomInternalServerException(buildHubExceptionMessage(e, pageNumber));
            }
            log.error("Unexpected hub error for page {}", pageNumber, e);
            throw new CustomInternalServerException(buildHubExceptionMessage(e, pageNumber));
        }
    }

  /*  private PageableIntegrationResponseDTO<SchotoborotGetPageableResponse> fetchPage(SchotoborotFilter filter, int pageNumber, int pageSize) {
        try {
            SchotoborotGetPageableRequest request = new SchotoborotGetPageableRequest();
            request.setIStartDate(filter.getStartDate());
            request.setIEndDate(filter.getEndDate());
            request.setIAccount(filter.getAccount());
            request.setIPageNumber(pageNumber);
            request.setIPageSize(pageSize);

            String hubResponse = hubClient.getAllExcel(request);
            log.info("Hub response: {}", hubResponse);

            ObjectMapper objectMapper = new ObjectMapper();
            JavaType type = objectMapper.getTypeFactory()
                    .constructParametricType(
                            PageableIntegrationResponseDTO.class,
                            SchotoborotGetPageableResponse.class
                    );

            return objectMapper.readValue(hubResponse, type);

        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }*/

    private byte[] zipCsvObjectsFromMinio(String folderName) throws IOException {
        List<String> objects = minioService.listCsvObjects(folderName);
        if (objects.isEmpty()) {
            throw new IOException("No CSV objects found to zip");
        }
        objects.sort(this::comparePageObjects);
        String zipRoot = buildZipRoot(folderName);
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ZipOutputStream zos = new ZipOutputStream(baos)) {
            for (String objectPath : objects) {
                String fileName = objectPath.substring(objectPath.lastIndexOf('/') + 1);
                ZipEntry entry = new ZipEntry(zipRoot + "/" + fileName);
                zos.putNextEntry(entry);
                try (InputStream inputStream = minioService.getCsvObjectStream(objectPath)) {
                    inputStream.transferTo(zos);
                }
                zos.closeEntry();
            }
            zos.finish();
            return baos.toByteArray();
        }
    }

    private byte[] zipExcelObjectsFromMinio(String folderName) throws IOException {
        List<String> objects = minioService.listExcelObjects(folderName);
        if (objects.isEmpty()) {
            throw new IOException("No Excel objects found to zip");
        }
        objects.sort(this::comparePageObjects);
        String zipRoot = buildZipRoot(folderName);
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ZipOutputStream zos = new ZipOutputStream(baos)) {
            for (String objectPath : objects) {
                String fileName = objectPath.substring(objectPath.lastIndexOf('/') + 1);
                ZipEntry entry = new ZipEntry(zipRoot + "/" + fileName);
                zos.putNextEntry(entry);
                try (InputStream inputStream = minioService.getExcelObjectStream(objectPath)) {
                    inputStream.transferTo(zos);
                }
                zos.closeEntry();
            }
            zos.finish();
            return baos.toByteArray();
        }
    }

    private String formatPageFileName(String extension, int page) {
        return String.format("schotoborot_page_%04d.%s", page, extension);
    }

    private int comparePageObjects(String left, String right) {
        Integer leftPage = extractPageNumber(left);
        Integer rightPage = extractPageNumber(right);
        if (leftPage != null && rightPage != null) {
            return Integer.compare(leftPage, rightPage);
        }
        return left.compareToIgnoreCase(right);
    }

    private String buildZipRoot(String folderName) {
        if (folderName == null || folderName.isBlank()) {
            return "schotoborot";
        }
        int maxLength = 32;
        if (folderName.length() <= maxLength) {
            return folderName;
        }
        String hash = Integer.toHexString(folderName.hashCode());
        String trimmed = folderName.substring(0, maxLength - 1 - hash.length());
        return trimmed + "_" + hash;
    }

    private Integer extractPageNumber(String objectPath) {
        int nameStart = objectPath.lastIndexOf('/') + 1;
        if (nameStart <= 0 || nameStart >= objectPath.length()) {
            return null;
        }
        String name = objectPath.substring(nameStart);
        int markerIndex = name.indexOf("schotoborot_page_");
        if (markerIndex < 0) {
            return null;
        }
        int numberStart = markerIndex + "schotoborot_page_".length();
        int dotIndex = name.indexOf('.', numberStart);
        if (dotIndex < 0) {
            return null;
        }
        String numberPart = name.substring(numberStart, dotIndex);
        try {
            return Integer.parseInt(numberPart);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private String buildFolderName(String username, SchotoborotFilter filter) {
        String safeUsername = username == null ? "anonymous" : username.trim();
        if (safeUsername.length() > 30) {
            safeUsername = safeUsername.substring(0, 30);
        }
        String raw = String.format("%s_%s_%s_%s_%s",
                SERVICE_NAME, safeUsername, filter.getAccount(), filter.getStartDate(), filter.getEndDate());
        return raw.replaceAll("[^A-Za-z0-9._-]", "_");
    }

    private boolean shouldCheckCache(SchotoborotFilter filter) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        LocalDate endDate = LocalDate.parse(filter.getEndDate(), formatter);
        LocalDate today = LocalDate.now(ZoneId.of("Asia/Tashkent"));
        return endDate.isBefore(today);
    }

    private boolean isInvalidDateFormat(String date) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            LocalDate.parse(date, formatter);
            return false;
        } catch (DateTimeParseException e) {
            return true;
        }
    }

    private boolean isTimeoutException(Throwable throwable) {
        Throwable current = throwable;
        while (current != null) {
            if (current instanceof RetryableException
                    || current instanceof SocketTimeoutException
                    || current instanceof TimeoutException) {
                return true;
            }
            String message = current.getMessage();
            if (message != null) {
                String normalized = message.toLowerCase();
                if (normalized.contains("read timed out")
                        || normalized.contains("connect timed out")
                        || normalized.contains("timeout")) {
                    return true;
                }
            }
            current = current.getCause();
        }
        return false;
    }

    private String buildHubExceptionMessage(Throwable throwable, int pageNumber) {
        if (throwable instanceof ResponseStatusException responseStatusException && StringUtils.hasText(responseStatusException.getReason())) {
            return responseStatusException.getReason();
        }
        if (isTimeoutException(throwable)) {
            return "Hub request timed out while fetching export data for page " + pageNumber + ". Please retry later or reduce the date range.";
        }
        Throwable current = throwable;
        while (current != null) {
            if (StringUtils.hasText(current.getMessage())) {
                return current.getMessage();
            }
            current = current.getCause();
        }
        return "Unexpected error while fetching export data from hub";
    }

    private ResponseEntity<ByteArrayResource> buildExcelResponse(byte[] bytes, String url) {
        String fileName = url.substring(url.lastIndexOf('/') + 1);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                .contentType(MediaType.parseMediaType(
                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .contentLength(bytes.length)
                .body(new ByteArrayResource(bytes));
    }

    private ResponseEntity<ByteArrayResource> buildCsvZipResponse(byte[] bytes, String fileName) {
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                .contentType(MediaType.parseMediaType("application/zip"))
                .contentLength(bytes.length)
                .body(new ByteArrayResource(bytes));
    }

    private ResponseEntity<ByteArrayResource> buildExcelZipResponse(byte[] bytes, String fileName) {
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                .contentType(MediaType.parseMediaType("application/zip"))
                .contentLength(bytes.length)
                .body(new ByteArrayResource(bytes));
    }

    public void validateFilter(SchotoborotFilter filter) {
        if (!StringUtils.hasText(filter.getStartDate())) {
            throw new BadRequestException("Start date is required");
        }
        if (isInvalidDateFormat(filter.getStartDate())) {
            throw new BadRequestException("Start date format incorrect. Use dd.MM.yyyy");
        }

        if (!StringUtils.hasText(filter.getEndDate())) {
            throw new BadRequestException("End date is required");
        }
        if (isInvalidDateFormat(filter.getEndDate())) {
            throw new BadRequestException("End date format incorrect. Use dd.MM.yyyy");
        }

        DateTimeFormatter hubFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        LocalDate start = LocalDate.parse(filter.getStartDate(), hubFormatter);
        LocalDate end = LocalDate.parse(filter.getEndDate(), hubFormatter);
        if (start.isAfter(end)) {
            throw new BadRequestException("Start date must be less than or equal to end date");
        }
        if (!StringUtils.hasText(filter.getAccount())) {
            throw new BadRequestException("Account is required");
        }
        if (filter.getAccount().length() != 20) {
            throw new BadRequestException("Account must be 20 characters long");
        }
        if (filter.getPageNumber() == null || filter.getPageNumber() <= 0) {
            filter.setPageNumber(1);
        }
        if (filter.getPageSize() == null || filter.getPageSize() <= 0) {
            filter.setPageSize(20);
        }
    }
}

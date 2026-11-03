package uz.davrbank.korinapogashenyaservice.aggregate.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import uz.davrbank.korinapogashenyaservice.aggregate.dto.integration.base.PageableIntegrationResponseDTO;
import uz.davrbank.korinapogashenyaservice.aggregate.dto.integration.request.PogasheniyaGetPageableRequest;
import uz.davrbank.korinapogashenyaservice.aggregate.dto.integration.response.PogasheniyaGetPageableResponse;
import uz.davrbank.korinapogashenyaservice.aggregate.filter.PogasheniyaFilter;
import uz.davrbank.korinapogashenyaservice.aggregate.integration.PogasheniyaHubClient;
import uz.davrbank.korinapogashenyaservice.aggregate.service.PogasheniyaFileGenerator;
import uz.davrbank.korinapogashenyaservice.aggregate.service.PogasheniyaService;
import uz.davrbank.korinapogashenyaservice.exception.condition.BadRequestException;
import uz.davrbank.korinapogashenyaservice.exception.condition.NotFoundException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class PogasheniyaServiceImpl implements PogasheniyaService {

    PogasheniyaHubClient hubClient;
    PogasheniyaFileGenerator fileGenerator;
    ObjectMapper objectMapper;

    static final int MAX_PAGE_SIZE = 100_000;

    @Override
    public ResponseEntity<PageableIntegrationResponseDTO<PogasheniyaGetPageableResponse>> pogasheniyaGetPageable(
            PogasheniyaFilter filter,
            String username
    ) {
        log.info("Get all pagination. username: {}, repDate: {}, state: {}, page: {}, size: {}",
                username, filter.getRepDate(), filter.getState(), filter.getPageNumber(), filter.getPageSize());
        validateFilter(filter);
        PageableIntegrationResponseDTO<PogasheniyaGetPageableResponse> response;
        try {
            PogasheniyaGetPageableRequest request = new PogasheniyaGetPageableRequest();
            request.setRepDate(filter.getRepDate());
            request.setState(filter.getState());
            request.setPageNumber(Optional.ofNullable(filter.getPageNumber()).orElse(1));
            request.setPageSize(Optional.ofNullable(filter.getPageSize()).orElse(20));
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
    public ResponseEntity<ByteArrayResource> pogasheniyaDownloadExcel(PogasheniyaFilter filter, String username) {
        log.info("Generating Excel report. username: {}, repDate: {}, state: {}",
                username, filter.getRepDate(), filter.getState());

        validateFilter(filter);

        List<PogasheniyaGetPageableResponse> result = getAll(filter);
        if (CollectionUtils.isEmpty(result)) {
            throw new NotFoundException("No data found for the given filter");
        }
        byte[] content;
        try {
            content = fileGenerator.generate(result, username, filter.getRepDate());
        } catch (Exception e) {
            log.error("Failed to generate report file: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to generate report file");
        }
        return buildExcelResponse(content, buildExcelFileName(filter));
    }

    private List<PogasheniyaGetPageableResponse> getAll(PogasheniyaFilter filter) {
        List<PogasheniyaGetPageableResponse> result = new ArrayList<>();
        int page = 1;
        int totalFetched = 0;
        final int maxPageLimit = 1000;
        boolean lastPage;

        do {
            long currentTime = System.currentTimeMillis();
            PageableIntegrationResponseDTO<PogasheniyaGetPageableResponse> response = fetchPage(filter, page, MAX_PAGE_SIZE);
            log.info("Fetched page {} in {} ms", page, System.currentTimeMillis() - currentTime);
            if (response.getData() == null || response.getData().isEmpty()) {
                log.warn("No data returned for page {}", page);
                break;
            }
            result.addAll(response.getData());
            totalFetched += response.getData().size();
            lastPage = Optional.ofNullable(response.getLast()).orElse(Boolean.FALSE);
            if (page >= maxPageLimit) {
                log.warn("Max page limit reached");
                break;
            }
            page++;
        } while (!lastPage);

        log.info("Fetched {} records", totalFetched);
        return result;
    }

    private PageableIntegrationResponseDTO<PogasheniyaGetPageableResponse> fetchPage(
            PogasheniyaFilter filter,
            int pageNumber,
            int pageSize
    ) {
        try {
            PogasheniyaGetPageableRequest request = new PogasheniyaGetPageableRequest();
            request.setRepDate(filter.getRepDate());
            request.setState(filter.getState());
            request.setPageNumber(pageNumber);
            request.setPageSize(pageSize);
            return hubClient.getAll(request);
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
            throw new RuntimeException("Error: " + e.getMessage());
        }
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

    private ResponseEntity<ByteArrayResource> buildExcelResponse(byte[] bytes, String fileName) {
        String asciiFileName = toAsciiFileName(fileName);
        String encodedFileName = encodeRFC5987(fileName);
        String contentDisposition = "attachment; filename=\"" + asciiFileName + "\"; filename*=UTF-8''" + encodedFileName;
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .contentType(MediaType.parseMediaType(
                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .contentLength(bytes.length)
                .body(new ByteArrayResource(bytes));
    }

    private String buildExcelFileName(PogasheniyaFilter filter) {
        String repDate = filter.getRepDate() != null ? filter.getRepDate() : "all";
        return "Korzina grafika pogasheniya " + repDate + ".xlsx";
    }

    private String encodeRFC5987(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8).replace("+", "%20");
    }

    private String toAsciiFileName(String value) {
        StringBuilder builder = new StringBuilder(value.length());
        for (int i = 0; i < value.length(); i++) {
            char ch = value.charAt(i);
            if (ch >= 0x20 && ch <= 0x7E && ch != '"' && ch != '\\') {
                builder.append(ch);
            } else {
                builder.append('_');
            }
        }
        return builder.toString();
    }

    public void validateFilter(PogasheniyaFilter filter) {
        if (!StringUtils.hasText(filter.getRepDate())) {
            throw new BadRequestException("RepDate is required");
        }
        if (isInvalidDateFormat(filter.getRepDate())) {
            throw new BadRequestException("RepDate format incorrect. Use dd.MM.yyyy");
        }
        if (filter.getState() == null) {
            throw new BadRequestException("State must not be null");
        }
        if (filter.getPageNumber() == null || filter.getPageNumber() <= 0) {
            filter.setPageNumber(1);
        }
        if (filter.getPageSize() == null || filter.getPageSize() <= 0) {
            filter.setPageSize(20);
        }
    }
}

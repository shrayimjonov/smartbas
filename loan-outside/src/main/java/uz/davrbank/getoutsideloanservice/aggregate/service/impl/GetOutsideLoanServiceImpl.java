package uz.davrbank.getoutsideloanservice.aggregate.service.impl;

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
import uz.davrbank.getoutsideloanservice.aggregate.dto.integration.base.PageableIntegrationResponseDTO;
import uz.davrbank.getoutsideloanservice.aggregate.dto.integration.request.GetOutsideLoanPageableRequest;
import uz.davrbank.getoutsideloanservice.aggregate.dto.integration.response.GetOutsideLoanPageableResponse;
import uz.davrbank.getoutsideloanservice.aggregate.filter.GetOutsideLoanFilter;
import uz.davrbank.getoutsideloanservice.aggregate.integration.GetOutsideLoanHubClient;
import uz.davrbank.getoutsideloanservice.aggregate.service.GetOutsideLoanFileGenerator;
import uz.davrbank.getoutsideloanservice.aggregate.service.GetOutsideLoanService;
import uz.davrbank.getoutsideloanservice.exception.condition.BadRequestException;
import uz.davrbank.getoutsideloanservice.exception.condition.CustomInternalServerException;
import uz.davrbank.getoutsideloanservice.exception.condition.NotFoundException;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GetOutsideLoanServiceImpl implements GetOutsideLoanService {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    private static final Set<Integer> AVAILABLE_STATES = Set.of(0, 1, 2);
    private static final int DEFAULT_PAGE_NUMBER = 1;
    private static final int DEFAULT_PAGE_SIZE = 20;
    private static final int MAX_PAGE_SIZE = 200_000;
    private static final int EXCEL_MAX_PAGE_LIMIT = 1_000;

    GetOutsideLoanHubClient hubClient;
    GetOutsideLoanFileGenerator fileGenerator;

    @Override
    public ResponseEntity<PageableIntegrationResponseDTO<GetOutsideLoanPageableResponse>> getAllPageable(
            GetOutsideLoanFilter filter,
            String username
    ) {
        log.info(
                "Get outside loan page. username: {}, startDate: {}, endDate: {}, branch: {}, state: {}, page: {}, size: {}",
                username,
                filter.getStartDate(),
                filter.getEndDate(),
                filter.getBranch(),
                filter.getState(),
                filter.getPageNumber(),
                filter.getPageSize()
        );

        validateFilter(filter, false);
        GetOutsideLoanPageableRequest request = buildRequest(filter);

        try {
            log.info("Hub page request: {}", request);
            PageableIntegrationResponseDTO<GetOutsideLoanPageableResponse> response = hubClient.getPage(request);
            if (response == null) {
                throw new CustomInternalServerException("Empty response returned from hub service");
            }
            if (!StringUtils.hasText(response.getStatus())) {
                response.setStatus("success");
            }
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Failed to fetch page data: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public ResponseEntity<ByteArrayResource> downloadExcel(GetOutsideLoanFilter filter, String username) {
        log.info(
                "Download excel report. username: {}, startDate: {}, endDate: {}, branch: {}, state: {}",
                username,
                filter.getStartDate(),
                filter.getEndDate(),
                filter.getBranch(),
                filter.getState()
        );

        validateFilter(filter, true);
        List<GetOutsideLoanPageableResponse> result = getAll(filter);
        if (CollectionUtils.isEmpty(result)) {
            throw new NotFoundException("No data found for the given filter");
        }

        byte[] content;
        try {
            content = fileGenerator.generate(result, username, buildReportPeriod(filter));
        } catch (Exception e) {
            log.error("Failed to generate report file: {}", e.getMessage(), e);
            throw new CustomInternalServerException("Failed to generate report file");
        }
        return buildExcelResponse(content, buildExcelFileName(filter));
    }

    private List<GetOutsideLoanPageableResponse> getAll(GetOutsideLoanFilter filter) {
        List<GetOutsideLoanPageableResponse> result = new ArrayList<>();
        int page = DEFAULT_PAGE_NUMBER;
        int totalFetched = 0;
        boolean lastPage;

        do {
            long currentTime = System.currentTimeMillis();
            PageableIntegrationResponseDTO<GetOutsideLoanPageableResponse> response = fetchPage(filter, page, MAX_PAGE_SIZE);
            log.info("Fetched excel page {} in {} ms", page, System.currentTimeMillis() - currentTime);

            List<GetOutsideLoanPageableResponse> pageData = response.getData();
            if (CollectionUtils.isEmpty(pageData)) {
                log.warn("No data returned for excel page {}", page);
                break;
            }

            result.addAll(pageData);
            totalFetched += pageData.size();
            lastPage = Optional.ofNullable(response.getLast()).orElse(Boolean.FALSE);

            if (page >= EXCEL_MAX_PAGE_LIMIT) {
                log.warn("Excel max page limit reached");
                break;
            }

            page++;
        } while (!lastPage);

        log.info("Fetched {} records for excel export", totalFetched);

        return result;
    }

    private PageableIntegrationResponseDTO<GetOutsideLoanPageableResponse> fetchPage(
            GetOutsideLoanFilter filter,
            int pageNumber,
            int pageSize
    ) {
        GetOutsideLoanPageableRequest request = buildRequest(filter, pageNumber, pageSize);

        try {
            log.info("Hub excel page request: {}", request);
            PageableIntegrationResponseDTO<GetOutsideLoanPageableResponse> response = hubClient.getPage(request);
            if (response == null) {
                throw new CustomInternalServerException("Empty response returned from hub service");
            }
            return response;
        } catch (Exception e) {
            log.error("Failed to fetch excel page {}: {}", pageNumber, e.getMessage(), e);
            throw e;
        }
    }

    private GetOutsideLoanPageableRequest buildRequest(GetOutsideLoanFilter filter) {
        return buildRequest(filter, filter.getPageNumber(), filter.getPageSize());
    }

    private GetOutsideLoanPageableRequest buildRequest(GetOutsideLoanFilter filter, Integer pageNumber, Integer pageSize) {
        return new GetOutsideLoanPageableRequest(
                filter.getStartDate(),
                filter.getEndDate(),
                normalizeBranch(filter.getBranch()),
                filter.getState(),
                pageNumber,
                pageSize
        );
    }

    private void validateFilter(GetOutsideLoanFilter filter, boolean forExcel) {
        if (filter == null) {
            throw new BadRequestException("Filter is required");
        }
        if (!StringUtils.hasText(filter.getStartDate())) {
            throw new BadRequestException("StartDate is required");
        }
        if (!StringUtils.hasText(filter.getEndDate())) {
            throw new BadRequestException("EndDate is required");
        }
        if (isInvalidDateFormat(filter.getStartDate())) {
            throw new BadRequestException("StartDate format incorrect. Use dd.MM.yyyy");
        }
        if (isInvalidDateFormat(filter.getEndDate())) {
            throw new BadRequestException("EndDate format incorrect. Use dd.MM.yyyy");
        }
        if (parseDate(filter.getEndDate()).isBefore(parseDate(filter.getStartDate()))) {
            throw new BadRequestException("EndDate must not be before StartDate");
        }
        if (filter.getState() == null) {
            throw new BadRequestException("State is required");
        }
        if (!AVAILABLE_STATES.contains(filter.getState())) {
            throw new BadRequestException("State should only be 0, 1, 2");
        }
        if (filter.getPageNumber() == null || filter.getPageNumber() <= 0) {
            filter.setPageNumber(DEFAULT_PAGE_NUMBER);
        }
        if (filter.getPageSize() == null || filter.getPageSize() <= 0) {
            filter.setPageSize(forExcel ? MAX_PAGE_SIZE : DEFAULT_PAGE_SIZE);
        }
        if (!forExcel && filter.getPageSize() > MAX_PAGE_SIZE) {
            throw new BadRequestException("PageSize should not exceed " + MAX_PAGE_SIZE);
        }
    }

    private boolean isInvalidDateFormat(String value) {
        try {
            parseDate(value);
            return false;
        } catch (DateTimeParseException e) {
            return true;
        }
    }

    private LocalDate parseDate(String value) {
        return LocalDate.parse(value, DATE_FORMATTER);
    }

    private String normalizeBranch(String branch) {
        return StringUtils.hasText(branch) ? branch.trim() : null;
    }

    private String buildReportPeriod(GetOutsideLoanFilter filter) {
        return "Портфел полученных кредитов на период c %s по %s"
                .formatted(filter.getStartDate(), filter.getEndDate());
    }

    private String buildExcelFileName(GetOutsideLoanFilter filter) {
        return "Портфел полученных кредитов c %s по %s.xlsx"
                .formatted(filter.getStartDate(), filter.getEndDate());
    }

    private ResponseEntity<ByteArrayResource> buildExcelResponse(byte[] bytes, String fileName) {
        String asciiFileName = toAsciiFileName(fileName);
        String encodedFileName = encodeRFC5987(fileName);
        String contentDisposition = "attachment; filename=\"" + asciiFileName + "\"; filename*=UTF-8''" + encodedFileName;
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .contentLength(bytes.length)
                .body(new ByteArrayResource(bytes));
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
}
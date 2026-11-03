package uz.davrbank.zalogautoservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import uz.davrbank.zalogautoservice.aggregate.dto.*;
import uz.davrbank.zalogautoservice.feign.AutoZalogFeign;
import uz.davrbank.zalogautoservice.service.interfaces.AutoServiceInterface;

import java.util.Collections;
import java.util.regex.Pattern;

@Service
@Slf4j
@RequiredArgsConstructor
public class AutoService implements AutoServiceInterface {

    private final AutoZalogFeign feign;

    private static final Pattern PINFL_PATTERN = Pattern.compile("^\\d{9}$|^\\d{14}$");

    private String normalizePinfl(String pinfl) {
        if (pinfl == null || pinfl.isBlank()) {
            return null;
        }
        String cleaned = pinfl.replaceAll("\\D", ""); // faqat raqamlar
        if (PINFL_PATTERN.matcher(cleaned).matches()) {
            return cleaned;
        }
        log.warn("Invalid PINFL format: {} (length: {})", pinfl, cleaned.length());
        return null;
    }

    private String normalizeString(String str) {
        return str != null ? str.trim() : null;
    }

    private void validatePage(Integer page, Integer size) {
        if (page != null && page < 1) {
            throw new IllegalArgumentException("pageNumber must be >= 1");
        }
        if (size != null && size < 1) {
            throw new IllegalArgumentException("pageSize must be >= 1");
        }
    }

    @Override
    public AutoResponseDto getAutoZalogReport(AutoRequestDto req) {
        log.info("getAutoZalogReport – state={}, pinfl={}, branch={}", req.getState(), req.getPinfl(), req.getBranch());

        String pinfl = normalizePinfl(req.getPinfl());
        if (req.getPinfl() != null && pinfl == null) {
            return errorResponse("PINFL must be exactly 9 or 14 digits");
        }

        try {
            return feign.getAutoZalogReport(
                    normalizeString(req.getStartDate()),
                    normalizeString(req.getEndDate()),
                    req.getState(),
                    normalizeString(req.getBranch()),
                    req.getLoanId(),
                    normalizeString(req.getClientId()),
                    pinfl,
                    normalizeString(req.getClientName()),
                    normalizeString(req.getStateNumber()),
                    normalizeString(req.getAddress())
            );
        } catch (ResponseStatusException ex) {
            throw ex;
        } catch (Exception e) {
            log.error("Error in getAutoZalogReport", e);
            return errorResponse(e.getMessage());
        }
    }

    @Override
    public AutoPaginatedResponseDto getAutoZalogReportPaginated(AutoRequestDto req) {
        log.info("getAutoZalogReportPaginated – page={}, size={}, pinfl={}",
                req.getPageNumber(), req.getPageSize(), req.getPinfl());

        validatePage(req.getPageNumber(), req.getPageSize());

        String pinfl = normalizePinfl(req.getPinfl());
        if (req.getPinfl() != null && pinfl == null) {
            return errorPaginated("PINFL must be exactly 9 or 14 digits");
        }

        Integer page = req.getPageNumber() != null ? req.getPageNumber() : 1;
        Integer size = req.getPageSize() != null ? req.getPageSize() : 10;

        try {
            return feign.getAutoZalogReportPaginated(
                    normalizeString(req.getStartDate()),
                    normalizeString(req.getEndDate()),
                    req.getState(),
                    page,
                    size,
                    normalizeString(req.getBranch()),
                    req.getLoanId(),
                    normalizeString(req.getClientId()),
                    pinfl,
                    normalizeString(req.getClientName()),
                    normalizeString(req.getStateNumber()),
                    normalizeString(req.getAddress())
            );
        } catch (ResponseStatusException ex) {
            throw ex;
        } catch (Exception e) {
            log.error("Error in getAutoZalogReportPaginated", e);
            return errorPaginated(e.getMessage());
        }
    }

    @Override
    public AutoPaginatedResponseDto getAutoZalogReportPaginatedSort(AutoPaginatedSortRequestDto req) {
        log.info("getAutoZalogReportPaginatedSort – sort={}, pinfl={}", req.getSort(), req.getPinfl());

        validatePage(req.getPageNumber(), req.getPageSize());

        if (req.getSort() == null || req.getSort().trim().isEmpty()) {
            return errorPaginated("sort parameter is required");
        }

        String pinfl = normalizePinfl(req.getPinfl());
        if (req.getPinfl() != null && pinfl == null) {
            return errorPaginated("PINFL must be exactly 9 or 14 digits");
        }

        Integer page = req.getPageNumber() != null ? req.getPageNumber() : 1;
        Integer size = req.getPageSize() != null ? req.getPageSize() : 10;

        try {
            return feign.getAutoZalogReportPaginatedSort(
                    normalizeString(req.getStartDate()),
                    normalizeString(req.getEndDate()),
                    req.getState(),
                    page,
                    size,
                    req.getSort(),
                    normalizeString(req.getBranch()),
                    req.getLoanId(),
                    normalizeString(req.getClientId()),
                    pinfl,
                    normalizeString(req.getClientName()),
                    normalizeString(req.getStateNumber()),
                    normalizeString(req.getAddress())
            );
        } catch (ResponseStatusException ex) {
            throw ex;
        } catch (Exception e) {
            log.error("Error in getAutoZalogReportPaginatedSort", e);
            return errorPaginated(e.getMessage());
        }
    }

    private AutoResponseDto errorResponse(String msg) {
        AutoResponseDto err = new AutoResponseDto();
        err.setStatus("error");
        err.setError(msg);
        err.setTotalElements(0);
        err.setData(Collections.emptyList());
        return err;
    }

    private AutoPaginatedResponseDto errorPaginated(String msg) {
        AutoPaginatedResponseDto err = new AutoPaginatedResponseDto();
        err.setStatus("error");
        err.setError(msg);
        err.setTotalElements(0);
        err.setTotalPages(0);
        err.setPageNumber(1);
        err.setPageSize(10);
        err.setNumberOfElements(0);
        err.setFirst(true);
        err.setLast(true);
        err.setEmpty(true);
        err.setData(Collections.emptyList());
        return err;
    }
}
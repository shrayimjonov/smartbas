package uz.davrbank.zalogautoservice.feign.config;

import org.springframework.stereotype.Component;
import uz.davrbank.zalogautoservice.aggregate.dto.AutoPaginatedResponseDto;
import uz.davrbank.zalogautoservice.aggregate.dto.AutoResponseDto;
import uz.davrbank.zalogautoservice.feign.AutoZalogFeign;

import java.util.ArrayList;
import java.util.Collections;

@Component
public class ZalogAutoClientFallback implements AutoZalogFeign {

    private static final String FALLBACK_MSG = "Hub service unavailable. Fallback response returned.";

    @Override
    public AutoResponseDto getAutoZalogReport(
            String startDate, String endDate, Integer state,
            String branch, Integer loanId, String clientId,
            String pinfl, String clientName, String stateNumber, String address) {

        AutoResponseDto response = new AutoResponseDto();
        response.setStatus("error");
        response.setError(FALLBACK_MSG);
        response.setTotalElements(0);
        response.setData(Collections.emptyList());

        return response;
    }

    @Override
    public AutoPaginatedResponseDto getAutoZalogReportPaginated(
            String startDate, String endDate, Integer state,
            Integer pageNumber, Integer pageSize,
            String branch, Integer loanId, String clientId,
            String pinfl, String clientName, String stateNumber, String address) {

        return buildPaginatedFallback(pageNumber, pageSize);
    }

    @Override
    public AutoPaginatedResponseDto getAutoZalogReportPaginatedSort(
            String startDate, String endDate, Integer state,
            Integer pageNumber, Integer pageSize, String sort,
            String branch, Integer loanId, String clientId,
            String pinfl, String clientName, String stateNumber, String address) {

        return buildPaginatedFallback(pageNumber, pageSize);
    }

    private AutoPaginatedResponseDto buildPaginatedFallback(Integer pageNumber, Integer pageSize) {
        AutoPaginatedResponseDto response = new AutoPaginatedResponseDto();

        response.setStatus("error");
        response.setError(FALLBACK_MSG);
        response.setTotalElements(0);
        response.setTotalPages(0);
        response.setPageNumber(normalizePageNumber(pageNumber));
        response.setPageSize(normalizePageSize(pageSize));
        response.setNumberOfElements(0);
        response.setFirst(true);
        response.setLast(true);
        response.setEmpty(true);
        response.setData(new ArrayList<>());

        return response;
    }

    /**
     * Ensures valid page number (minimum 1).
     */
    private int normalizePageNumber(Integer pageNumber) {
        return (pageNumber != null && pageNumber > 0) ? pageNumber : 1;
    }

    /**
     * Ensures valid page size (minimum 1, default 1000).
     */
    private int normalizePageSize(Integer pageSize) {
        return (pageSize != null && pageSize > 0) ? pageSize : 1000;
    }
}
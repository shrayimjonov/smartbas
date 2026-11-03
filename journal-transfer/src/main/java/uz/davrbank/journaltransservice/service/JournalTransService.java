package uz.davrbank.journaltransservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uz.davrbank.journaltransservice.aggregate.dto.request.PageableRequestDTO;
import uz.davrbank.journaltransservice.aggregate.dto.response.PaginatedResponseDTO;
import uz.davrbank.journaltransservice.exception.condition.DatabaseException;
import uz.davrbank.journaltransservice.exception.condition.ExternalHubServiceException;
import uz.davrbank.journaltransservice.feign.JournalTransFeign;
import uz.davrbank.journaltransservice.service.interfaces.JournalTransServiceInterface;

@Slf4j
@Service
@RequiredArgsConstructor
public class JournalTransService implements JournalTransServiceInterface {

    private final JournalTransFeign feign;

    private String normalize(String value) {
        return value != null ? value.trim() : null;
    }

    private void validatePage(Integer page, Integer size) {
        if (page == null || page < 1) {
            throw new IllegalArgumentException("iPageNumber must be >= 1");
        }
        if (size == null || size < 1) {
            throw new IllegalArgumentException("iPageSize must be >= 1");
        }
    }


    @Override
    public PaginatedResponseDTO getJournalTransReportPaginated(PageableRequestDTO dto) {
        try {
            log.info("Request → getJournalTransReportPaginated: {}", dto);

            validatePage(dto.getIPageNumber(), dto.getIPageSize());
            PaginatedResponseDTO resp = feign.getJournalTransReportPaginated(
                    normalize(dto.getIStartDate()),
                    normalize(dto.getIEndDate()),
                    normalize(dto.getIAccBalKod()),
                    normalize(dto.getIBranch()),
                    dto.getIPageNumber(),
                    dto.getIPageSize()
            );

            if (resp != null && "error".equalsIgnoreCase(resp.getStatus())) {
                log.warn("Response ← getJournalTransReportPaginated FALLBACK: {}", resp.getError());
                throw new ExternalHubServiceException(resp.getError());
            }
            log.info("Response ← getJournalTransReportPaginated SUCCESS");
            return resp;

        } catch (ExternalHubServiceException e) {
            throw e;

        } catch (Exception e) {
            log.error("ERROR in getJournalTransReportPaginated → {}", e.getMessage(), e);
            throw new DatabaseException("Failed to load paginated journal report");
        }
    }
}

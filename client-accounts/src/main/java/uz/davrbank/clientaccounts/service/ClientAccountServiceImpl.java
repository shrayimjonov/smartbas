package uz.davrbank.clientaccounts.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uz.davrbank.clientaccounts.aggregate.dto.BalanceCodeResponseDto;
import uz.davrbank.clientaccounts.aggregate.dto.ClientAccountPaginatedResponseDto;
import uz.davrbank.clientaccounts.aggregate.dto.ClientAccountRequestDto;
import uz.davrbank.clientaccounts.aggregate.dto.ClientAccountResponseDto;
import uz.davrbank.clientaccounts.feign.ClientAccountsHubFeign;
import uz.davrbank.clientaccounts.service.interfaces.ClientAccountService;

import java.util.Collections;

@Service
@Slf4j
@RequiredArgsConstructor
public class ClientAccountServiceImpl implements ClientAccountService {

    private final ClientAccountsHubFeign feign;

    @Override
    public BalanceCodeResponseDto<String> getAccBalKod() {
        try {
            BalanceCodeResponseDto<String> response = feign.getAccBalKod();
            if (response.getData() != null) {
                response.setTotalElements(response.getData().size());
            } else {
                response.setData(Collections.emptyList());
                response.setTotalElements(0);
            }
            return response;
        } catch (Exception e) {
            log.error("Failed to fetch balance codes from Hub", e);
            return new BalanceCodeResponseDto<>("error", "Service temporarily unavailable", 0, Collections.emptyList());
        }
    }

    @Override
    public ClientAccountResponseDto getAccountsNoPage(ClientAccountRequestDto dto) {
        validateRequired(dto);
        try {
            ClientAccountResponseDto response = feign.getAccountsNoPage(
                    dto.getIDate(), dto.getIBranch(), dto.getIClientType(),
                    dto.getIState(), dto.getIValType(), dto.getIAccList(),
                    dto.getIAccount(), dto.getIClientId()
            );
            if (response.getData() != null) {
                response.setTotalElements(response.getData().size());
            }
            return response;
        } catch (Exception e) {
            log.error("Failed to fetch accounts no page", e);
            return new ClientAccountResponseDto("error", e.getMessage(), 0, Collections.emptyList());
        }
    }

    @Override
    public ClientAccountPaginatedResponseDto getAccountsPage(ClientAccountRequestDto dto) {
        validateRequired(dto);
        validatePage(dto.getIPageNumber(), dto.getIPageSize());

        try {
            ClientAccountPaginatedResponseDto response = feign.getAccountsPage(
                    dto.getIDate(),
                    dto.getIBranch(),
                    dto.getIAccList(),
                    dto.getIClientType(),
                    dto.getIState(),
                    dto.getIValType(),
                    dto.getIAccount(),
                    dto.getIClientId(),
                    dto.getIPageNumber(),
                    dto.getIPageSize()
            );

            enrichPaginatedResponse(response);
            return response;

        } catch (Exception e) {
            log.error("Failed to call Hub /page API", e);
            return buildErrorPaginatedResponse(dto.getIPageNumber(), dto.getIPageSize());
        }
    }

    @Override
    public ClientAccountPaginatedResponseDto getAccountsPageSort(ClientAccountRequestDto dto) {
        validateRequired(dto);
        validatePage(dto.getIPageNumber(), dto.getIPageSize());
        if (dto.getISort() == null || dto.getISort().trim().isEmpty()) {
            throw new IllegalArgumentException("Sort parameter is required");
        }

        try {
            ClientAccountPaginatedResponseDto response = feign.getAccountsPageSort(
                    dto.getIDate(),
                    dto.getIBranch(),
                    dto.getIClientType(),
                    dto.getIState(),
                    dto.getIValType(),
                    dto.getIAccList(),
                    dto.getIAccount(),
                    dto.getIClientId(),
                    dto.getIPageNumber(),
                    dto.getIPageSize(),
                    dto.getISort()
            );

            enrichPaginatedResponse(response);
            return response;

        } catch (Exception e) {
            log.error("Failed to fetch sorted accounts page", e);
            return buildErrorPaginatedResponse(dto.getIPageNumber(), dto.getIPageSize());
        }
    }

    private void enrichPaginatedResponse(ClientAccountPaginatedResponseDto resp) {
        if (resp.getData() == null) {
            resp.setData(Collections.emptyList());
        }

        int dataSize = resp.getData().size();
        resp.setNumberOfElements(dataSize);

        if (resp.getTotalElements() == null || resp.getTotalElements() <= 0) {
            resp.setTotalElements(dataSize);
        }

        if (resp.getPageSize() != null && resp.getPageSize() > 0) {
            int total = resp.getTotalElements();
            resp.setTotalPages((int) Math.ceil((double) total / resp.getPageSize()));
        } else {
            resp.setTotalPages(1);
        }

        int page = resp.getPageNumber() != null ? resp.getPageNumber() : 1;
        resp.setPageNumber(page);
        resp.setFirst(page <= 1);
        resp.setLast(resp.getTotalPages() != null && page >= resp.getTotalPages());
        resp.setEmpty(dataSize == 0);

        if (resp.getStatus() == null) resp.setStatus("success");
        if (resp.getError() == null) resp.setError("");
    }

    private ClientAccountPaginatedResponseDto buildErrorPaginatedResponse(Integer pageNumber, Integer pageSize) {
        ClientAccountPaginatedResponseDto resp = new ClientAccountPaginatedResponseDto();
        resp.setStatus("error");
        resp.setError("Service temporarily unavailable");
        resp.setTotalElements(0);
        resp.setData(Collections.emptyList());
        resp.setTotalPages(0);
        resp.setPageNumber(pageNumber != null ? pageNumber : 1);
        resp.setPageSize(pageSize != null ? pageSize : 20);
        resp.setNumberOfElements(0);
        resp.setFirst(true);
        resp.setLast(true);
        resp.setEmpty(true);
        return resp;
    }

    private void validateRequired(ClientAccountRequestDto dto) {
        if (dto.getIDate() == null || dto.getIDate().trim().isEmpty())
            throw new IllegalArgumentException("iDate is required (format: dd.MM.yyyy)");
        if (dto.getIBranch() == null || dto.getIBranch().trim().isEmpty())
            throw new IllegalArgumentException("iBranch is required");
        if (dto.getIAccList() == null || dto.getIAccList().trim().isEmpty())
            throw new IllegalArgumentException("iAccList is required");
    }

    private void validatePage(Integer page, Integer size) {
        if (page == null || page < 1) throw new IllegalArgumentException("pageNumber must be >= 1");
        if (size == null || size < 1) throw new IllegalArgumentException("pageSize must be >= 1");
    }
}
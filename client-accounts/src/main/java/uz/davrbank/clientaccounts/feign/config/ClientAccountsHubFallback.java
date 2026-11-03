package uz.davrbank.clientaccounts.feign.config;

import org.springframework.stereotype.Component;
import uz.davrbank.clientaccounts.aggregate.dto.BalanceCodeResponseDto;
import uz.davrbank.clientaccounts.aggregate.dto.ClientAccountPaginatedResponseDto;
import uz.davrbank.clientaccounts.aggregate.dto.ClientAccountResponseDto;
import uz.davrbank.clientaccounts.feign.ClientAccountsHubFeign;

import java.util.Collections;

@Component
public class ClientAccountsHubFallback implements ClientAccountsHubFeign {

    private static final String MSG = "Client Accounts Hub temporarily unavailable. Please try again later.";

    @Override
    public BalanceCodeResponseDto<String> getAccBalKod() {
        return new BalanceCodeResponseDto<>("error", MSG, 0, Collections.emptyList());
    }

    @Override
    public ClientAccountResponseDto getAccountsNoPage(String iDate, String iBranch, Integer iClientType, Integer iState,
                                                      Integer iValType, String iAccList, String iAccount, String iClientId) {
        ClientAccountResponseDto resp = new ClientAccountResponseDto();
        resp.setStatus("error"); resp.setError(MSG); resp.setTotalElements(0); resp.setData(Collections.emptyList());
        return resp;
    }

    @Override
    public ClientAccountPaginatedResponseDto getAccountsPage(
            String iDate, String iBranch, String iAccList,
            Integer iClientType, Integer iState, Integer iValType,
            String iAccount, String iClientId,
            Integer iPageNumber, Integer iPageSize) {

        return buildPaginatedError(iPageNumber, iPageSize);
    }

    @Override
    public ClientAccountPaginatedResponseDto getAccountsPageSort(
            String iDate, String iBranch, Integer iClientType, Integer iState, Integer iValType,
            String iAccList, String iAccount, String iClientId,
            Integer iPageNumber, Integer iPageSize, String iSort) {
        return buildPaginatedError(iPageNumber, iPageSize);
    }

    private ClientAccountPaginatedResponseDto buildPaginatedError(Integer pageNumber, Integer pageSize) {
        ClientAccountPaginatedResponseDto resp = new ClientAccountPaginatedResponseDto();
        resp.setStatus("error");
        resp.setError(MSG);
        resp.setTotalElements(0);
        resp.setTotalPages(0);
        resp.setPageNumber(pageNumber != null ? pageNumber : 1);
        resp.setPageSize(pageSize != null ? pageSize : 20);
        resp.setNumberOfElements(0);
        resp.setFirst(true);
        resp.setLast(true);
        resp.setEmpty(true);
        resp.setData(Collections.emptyList());
        return resp;
    }
}
package uz.davrbank.clientaccounts.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import uz.davrbank.clientaccounts.aggregate.dto.*;
import uz.davrbank.clientaccounts.feign.config.ClientAccountsHubFallback;

@FeignClient(name = "clientaccountshub-service",
        fallback = ClientAccountsHubFallback.class)
public interface ClientAccountsHubFeign {

    @PostMapping("/hub/api/client/account/get/accode")
    BalanceCodeResponseDto<String> getAccBalKod();

    @PostMapping("/hub/api/client/account/get/accode/no/page")
    ClientAccountResponseDto getAccountsNoPage(
            @RequestParam("iDate") String iDate,
            @RequestParam("iBranch") String iBranch,
            @RequestParam("iClientType") Integer iClientType,
            @RequestParam("iState") Integer iState,
            @RequestParam("iValType") Integer iValType,
            @RequestParam("iAccList") String iAccList,
            @RequestParam(value = "iAccount", required = false) String iAccount,
            @RequestParam("iClientId") String iClientId
    );

    @PostMapping("/hub/api/client/account/get/accode/page")
    ClientAccountPaginatedResponseDto getAccountsPage(
            @RequestParam("iDate") String iDate,
            @RequestParam("iBranch") String iBranch,
            @RequestParam("iAccList") String iAccList,
            @RequestParam("iClientType") Integer iClientType,
            @RequestParam("iState") Integer iState,
            @RequestParam("iValType") Integer iValType,
            @RequestParam(value = "iAccount", required = false) String iAccount,
            @RequestParam(value = "iClientId", required = false) String iClientId,
            @RequestParam("iPageNumber") Integer iPageNumber,
            @RequestParam("iPageSize") Integer iPageSize
    );

    @PostMapping("/hub/api/client/account/get/accode/page/sort")
    ClientAccountPaginatedResponseDto getAccountsPageSort(
            @RequestParam("iDate") String iDate,
            @RequestParam("iBranch") String iBranch,
            @RequestParam("iClientType") Integer iClientType,
            @RequestParam("iState") Integer iState,
            @RequestParam("iValType") Integer iValType,
            @RequestParam("iAccList") String iAccList,
            @RequestParam(value = "iAccount", required = false) String iAccount,
            @RequestParam(value = "iClientId", required = false) String iClientId,
            @RequestParam("iPageNumber") Integer iPageNumber,
            @RequestParam("iPageSize") Integer iPageSize,
            @RequestParam("iSort") String iSort
    );
}
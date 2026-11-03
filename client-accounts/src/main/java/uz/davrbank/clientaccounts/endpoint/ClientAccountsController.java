package uz.davrbank.clientaccounts.endpoint;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uz.davrbank.clientaccounts.aggregate.dto.BalanceCodeResponseDto;
import uz.davrbank.clientaccounts.aggregate.dto.ClientAccountPaginatedResponseDto;
import uz.davrbank.clientaccounts.aggregate.dto.ClientAccountRequestDto;
import uz.davrbank.clientaccounts.aggregate.dto.ClientAccountResponseDto;
import uz.davrbank.clientaccounts.service.interfaces.ClientAccountService;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class ClientAccountsController {

    private final ClientAccountService service;

    @GetMapping("/accode")
    public ResponseEntity<BalanceCodeResponseDto<String>> getBalanceCodes() {
        return ResponseEntity.ok(service.getAccBalKod());
    }

    @GetMapping("/get/all")
    public ResponseEntity<ClientAccountResponseDto> getList(
            @RequestParam("iDate") String iDate,
            @RequestParam("iBranch") String iBranch,
            @RequestParam("iAccList") String iAccList,
            @RequestParam("iClientType") Integer iClientType,
            @RequestParam("iState") Integer iState,
            @RequestParam("iValType") Integer iValType,
            @RequestParam(value = "iAccount", required = false) String iAccount,
            @RequestParam("iClientId") String iClientId
    ) {
        ClientAccountRequestDto dto = ClientAccountRequestDto.builder()
                .iDate(iDate).iBranch(iBranch).iClientType(iClientType)
                .iState(iState).iValType(iValType).iAccList(iAccList)
                .iAccount(iAccount).iClientId(iClientId)
                .build();

        return ResponseEntity.ok(service.getAccountsNoPage(dto));
    }

    @GetMapping("/pagination")
    public ResponseEntity<ClientAccountPaginatedResponseDto> getPage(
            @RequestParam("iDate") String iDate,
            @RequestParam("iBranch") String iBranch,
            @RequestParam("iAccList") String iAccList,
            @RequestParam("iClientType") Integer iClientType,
            @RequestParam("iState") Integer iState,
            @RequestParam("iValType") Integer iValType,
            @RequestParam("iPageNumber") Integer iPageNumber,
            @RequestParam("iPageSize") Integer iPageSize,
            @RequestParam(value = "iAccount", required = false) String iAccount,
            @RequestParam(value = "iClientId", required = false) String iClientId) {

        ClientAccountRequestDto dto = ClientAccountRequestDto.builder()
                .iDate(iDate).iBranch(iBranch).iAccList(iAccList)
                .iClientType(iClientType).iState(iState).iValType(iValType)
                .iAccount(iAccount).iClientId(iClientId)
                .iPageNumber(iPageNumber).iPageSize(iPageSize)
                .build();

        return ResponseEntity.ok(service.getAccountsPage(dto));
    }

    @GetMapping("/pagination/sort")
    public ResponseEntity<ClientAccountPaginatedResponseDto> getPageSort(
            @RequestParam("iDate") String iDate,
            @RequestParam("iBranch") String iBranch,
            @RequestParam("iAccList") String iAccList,
            @RequestParam("iClientType") Integer iClientType,
            @RequestParam("iState") Integer iState,
            @RequestParam("iValType") Integer iValType,
            @RequestParam("iPageNumber") Integer iPageNumber,
            @RequestParam("iPageSize") Integer iPageSize,
            @RequestParam("iSort") String iSort,
            @RequestParam(value = "iAccount", required = false) String iAccount,
            @RequestParam(value = "iClientId", required = false) String iClientId) {

        ClientAccountRequestDto dto = ClientAccountRequestDto.builder()
                .iDate(iDate).iBranch(iBranch)
                .iClientType(iClientType).iState(iState).iValType(iValType)
                .iAccList(iAccList).iAccount(iAccount).iClientId(iClientId)
                .iPageNumber(iPageNumber).iPageSize(iPageSize)
                .iSort(iSort)
                .build();

        return ResponseEntity.ok(service.getAccountsPageSort(dto));
    }
}
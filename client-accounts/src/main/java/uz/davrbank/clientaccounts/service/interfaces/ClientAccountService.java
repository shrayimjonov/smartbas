package uz.davrbank.clientaccounts.service.interfaces;


import uz.davrbank.clientaccounts.aggregate.dto.BalanceCodeResponseDto;
import uz.davrbank.clientaccounts.aggregate.dto.ClientAccountPaginatedResponseDto;
import uz.davrbank.clientaccounts.aggregate.dto.ClientAccountRequestDto;
import uz.davrbank.clientaccounts.aggregate.dto.ClientAccountResponseDto;

public interface ClientAccountService {
    BalanceCodeResponseDto<String> getAccBalKod();

    ClientAccountResponseDto getAccountsNoPage(ClientAccountRequestDto dto);

    ClientAccountPaginatedResponseDto getAccountsPage(ClientAccountRequestDto dto);

    ClientAccountPaginatedResponseDto getAccountsPageSort(ClientAccountRequestDto dto);
}
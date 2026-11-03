package uz.davrbank.creditaccountsservice.service;

import uz.davrbank.creditaccountsservice.aggregate.request.CreditAccountRequestDTO;
import uz.davrbank.creditaccountsservice.aggregate.request.CreditRequestPageable;
import uz.davrbank.creditaccountsservice.aggregate.request.CreditRequestSorted;
import uz.davrbank.creditaccountsservice.aggregate.response.ResponseDTO;
import uz.davrbank.creditaccountsservice.aggregate.response.ResponsePaginationDTO;
import uz.davrbank.creditaccountsservice.aggregate.response.ResponseSortDTO;

public interface CreditAccountInterface {
    ResponseDTO getAllCreditAccounts(CreditAccountRequestDTO creditRequestParamDTO);

    ResponsePaginationDTO  getAllCreditAccountsPageable(CreditRequestPageable creditRequestPageable);

    ResponseSortDTO getAllCreditAccountsPageableSorted(CreditRequestSorted creditRequestSorted);
}

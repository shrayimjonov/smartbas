package uz.davrbank.yeisvoservice.service.service.interfaces;

import uz.davrbank.yeisvoservice.aggregate.dto.request.TestDtoReq;
import uz.davrbank.yeisvoservice.aggregate.dto.response.*;
import uz.davrbank.yeisvoservice.aggregate.dto.request.ContractListRequestBody;
import uz.davrbank.yeisvoservice.aggregate.dto.request.ContractRequestBody;


public interface ContractInterface {


    TestDtoRes checkContract(TestDtoReq request);

    ContractGetStatusRes getContractStatus(ContractRequestBody request);

    ContractGetRes updateContractDetails(ContractRequestBody request);

    ContractGetListWithPageResponse getContractList(ContractListRequestBody request);

    ContractGetOwnerDocRes getContractOwnerDocs(ContractRequestBody request);

    ContractGetHistoryRes getHistoryDocs( ContractRequestBody request);

    ContractGetRes getContract( ContractRequestBody request);

    ContractGetBankDocsRes getBankDocs( ContractRequestBody request);

}

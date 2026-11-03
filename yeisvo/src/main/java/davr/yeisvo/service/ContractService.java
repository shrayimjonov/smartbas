package davr.yeisvo.service;


import com.googlecode.jsonrpc4j.JsonRpcMethod;
import com.googlecode.jsonrpc4j.JsonRpcParam;
import com.googlecode.jsonrpc4j.JsonRpcService;
import davr.yeisvo.dto.TestDtoReq;
import davr.yeisvo.dto.TestDtoRes;
import davr.yeisvo.dto.request.ContractListRequestBody;
import davr.yeisvo.dto.request.ContractRequestBody;
import davr.yeisvo.dto.response.*;

@JsonRpcService("api/v1/contract")
public interface ContractService {

    @JsonRpcMethod("check")
    TestDtoRes checkContract(@JsonRpcParam("request") TestDtoReq request);

    // 1.1
    @JsonRpcMethod("contract.status")
    ContractGetStatusRes getContractStatus(@JsonRpcParam("request") ContractRequestBody request);


    @JsonRpcMethod("contract.update.details")
    ContractGetRes updateContractDetails(@JsonRpcParam("request")ContractRequestBody request);

    // 1.2
    @JsonRpcMethod("contract.list")
    ContractGetListWithPageResponse getContractList(@JsonRpcParam("request") ContractListRequestBody request);

    // 1.3
    @JsonRpcMethod("contract.owner.docs")
    ContractGetOwnerDocRes getContractOwnerDocs(@JsonRpcParam("request") ContractRequestBody request);


    // 1.4
    @JsonRpcMethod("contract.history")
    ContractGetHistoryRes getHistoryDocs(@JsonRpcParam("request") ContractRequestBody request);


    // 1.5
    @JsonRpcMethod("contract.get")
    ContractGetRes getContract(@JsonRpcParam("request") ContractRequestBody request);


    // 1.6
    @JsonRpcMethod("contract.bank.docs")
    ContractGetBankDocsRes getBankDocs(@JsonRpcParam("request") ContractRequestBody request);

}

package davr.yeisvo.service;

import com.googlecode.jsonrpc4j.JsonRpcMethod;
import com.googlecode.jsonrpc4j.JsonRpcParam;
import com.googlecode.jsonrpc4j.JsonRpcService;
import davr.yeisvo.dto.request.BankBranchRequest;
import davr.yeisvo.dto.response.BankBranchResponse;
import org.springframework.stereotype.Component;

import java.util.List;
@JsonRpcService("api/v1/branches")
@Component
public interface BankBranchService {


    @JsonRpcMethod("create.branch")
    BankBranchResponse create(@JsonRpcParam("request") BankBranchRequest request);

    @JsonRpcMethod("update.branch")
    BankBranchResponse update(@JsonRpcParam("request") BankBranchRequest request);

    @JsonRpcMethod("get.all.branches")
    List<BankBranchResponse> getAll();

    @JsonRpcMethod("get.branch.by.id")
    BankBranchResponse getById(@JsonRpcParam("id") Long id);

    @JsonRpcMethod("delete.branch")
    BankBranchResponse delete(@JsonRpcParam("id") Long id);
}

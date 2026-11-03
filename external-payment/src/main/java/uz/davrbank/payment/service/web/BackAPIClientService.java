package uz.davrbank.payment.service.web;


import uz.davrbank.payment.model.dto.backAPI.request.BackForexRequest;
import uz.davrbank.payment.model.dto.backAPI.request.BackInterBranchRequest;
import uz.davrbank.payment.model.dto.backAPI.request.BackSimpleTransactionRequest;
import uz.davrbank.payment.model.dto.backAPI.request.BackVFXRequest;
import uz.davrbank.payment.model.dto.backAPI.response.BackCheckTranResponse;
import uz.davrbank.payment.model.dto.backAPI.response.BackForexResponse;
import uz.davrbank.payment.model.dto.backAPI.response.BackSimpleTranResponse;
import uz.davrbank.payment.model.dto.backAPI.response.BackVFXResponse;
import uz.nodir.commondto.model.dto.core.response.SimpleResponse;

import java.util.List;

public interface BackAPIClientService {

    SimpleResponse<BackForexResponse> forex(BackForexRequest request);

    SimpleResponse<BackSimpleTranResponse> interBranch(BackInterBranchRequest request);

    SimpleResponse<BackSimpleTranResponse> simpleTransaction(BackSimpleTransactionRequest request);

    SimpleResponse<BackVFXResponse> vfxOperation(BackVFXRequest request);

    SimpleResponse<List<BackCheckTranResponse>> getTranInfoByExtId(String branch, String extId);
}

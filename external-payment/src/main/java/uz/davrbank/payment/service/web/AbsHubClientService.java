package uz.davrbank.payment.service.web;


import uz.davrbank.payment.model.dto.absHub.request.AbsHubTransactionReverseRequest;
import uz.davrbank.payment.model.dto.absHub.response.AbsHubTransactionReverseResponse;
import uz.nodir.commondto.model.dto.core.response.ResultData;
import uz.nodir.commondto.model.dto.core.response.SimpleResponse;

public interface AbsHubClientService {

    SimpleResponse<ResultData<AbsHubTransactionReverseResponse>> reverse(AbsHubTransactionReverseRequest request);
}

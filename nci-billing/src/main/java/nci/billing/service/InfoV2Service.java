package nci.billing.service;

import nci.billing.model.dto.CardsByPersonCodeResponseDTO;
import nci.billing.model.dto.DebtsDto;
import nci.billing.model.dto.LoanDto;
import nci.billing.model.dto.response.ResultData;
import nci.billing.model.request.TransactionRequest;
import nci.billing.model.response.*;
import nci.billing.model.response.nci.NCIClientInfoResponse;

import java.util.List;

public interface InfoV2Service {
    ResultData<LoanResponse> getLoanInfoV2(LoanDto loanExtId);

    ResultData<ClientResponse> getClientInfo(LoanDto loanExtId);

    ResultData<DebtsListResponse> getDebtsByBranch(DebtsDto branch);

    ResultData<List<TransactionResponse>> getLoanTransactions(TransactionRequest request);

    ResultData<List<CourseResponse>> getCourse();

    ResultData<List<CardsByPersonCodeResponseDTO>> getCardsByPersonCode(String personCode);

    ResultData<List<NCIClientInfoResponse>> getClientInfoByPINFL(String personCode);
}

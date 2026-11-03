package nci.billing.service;

import nci.billing.model.dto.DebtsDto;
import nci.billing.model.dto.LoanDto;
import nci.billing.model.dto.TransactionDto;
import nci.billing.model.dto.response.ResultData;
import nci.billing.model.request.TransactionRequest;
import nci.billing.model.response.*;

public interface InfoService {
    ClientResponse getClientDataByLoanId(LoanDto dto);

    LoanResponse getClientDebtInfoByLoanId(LoanDto dto);

    DebtsListResponse getDebtsListByBranch(DebtsDto dto);

    boolean getBankDayStatus(String branch);

    TransactionDto getLoanTransactions(TransactionRequest request);

    BankDayResponse getBankDayByBranch();

    GeneralResponse getTransactionById(String id);

}

package nci.billing.service;

import nci.billing.model.dto.*;
import nci.billing.model.response.CheckResponse;

public interface CoreService {
    CheckResponse checkTransaction(CheckPaymentDto request);

    ProblemDto pay(PayDto dto);

    ProblemDto reverseNCITransaction(ReverseDto dto);

    ProblemDto repayMainDebtByLoan(RepayMainDebtDto dto);

    ProblemDto repayMainDebtByLoanV2(RepayDTO dto);

    ProblemDto forexOperation(ForexTranRequestDTO requestDTO);

    ProblemDto simpleTransaction(SimpleTranRequestDTO requestDTO);
}

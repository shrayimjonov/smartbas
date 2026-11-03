package nci.billing.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nci.billing.exception.TimeOut;
import nci.billing.feign.NciGatewayFeign;
import nci.billing.model.dto.DebtsDto;
import nci.billing.model.dto.LoanDto;
import nci.billing.model.dto.ProblemDto;
import nci.billing.model.dto.TransactionDto;
import nci.billing.model.dto.response.ResultData;
import nci.billing.model.enums.ResponseCode;
import nci.billing.model.request.DebtsRequest;
import nci.billing.model.request.LoanInfoRequest;
import nci.billing.model.request.LoanRequest;
import nci.billing.model.request.TransactionRequest;
import nci.billing.model.response.*;
import nci.billing.service.InfoService;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class InfoImpl implements InfoService {
    private final NciGatewayFeign nciGatewayFeign;


    @Override
    public ClientResponse getClientDataByLoanId(LoanDto dto) {
        var request = LoanRequest.builder()
                .loanId(dto.getLoanId())
                .build();
        log.info("Nci_Gateway getClientDataByLoanId method request: {}", request);
        try {
            var response = nciGatewayFeign.getClientDataByLoanId(request);
            if (response == null)
                return ClientResponse.builder()
                        .error(returnResponse(ResponseCode.LOAN_NOT_FOUND))
                        .build();
            log.info("Nci_Gateway getClientDataByLoanId method response: {}", response);
            return response;
        } catch (Exception ex) {
            log.error("nciGateway getClientDataByLoanId method server throws error: %s".formatted(ex.getMessage()));
            throw new TimeOut("nciGateway getClientDataByLoanId method server throws error: %s".formatted(ex.getMessage()));
        }
    }

    @Override
    public LoanResponse getClientDebtInfoByLoanId(LoanDto dto) {
        var request = LoanRequest.builder()
                .loanId(dto.getLoanId())
                .build();
        log.info("Nci_Gateway getClientDebtInfoByLoanId method request: {}", request);
        try {
            var response = nciGatewayFeign.getClientDebtInfoByLoanId(request);
            log.info("Nci_Gateway getClientDebtInfoByLoanId method response: {}", response);
            return response;
        } catch (Exception ex) {
            log.error("nciGateway getClientDebtInfoByLoanId method server throws error: %s".formatted(ex.getMessage()));
            throw new TimeOut("nciGateway getClientDebtInfoByLoanId method server throws error: %s".formatted(ex.getMessage()));
        }
    }

    @Override
    public DebtsListResponse getDebtsListByBranch(DebtsDto dto) {
        var request = DebtsRequest.builder()
                .branch(dto.getBranch())
                .build();
        log.info("Bulk find starting..... by branch: {}", dto.getBranch());
        try {
            var response = nciGatewayFeign.getDebtsListByBranch(request);
            log.debug("Nci_Gateway getDebtsListByBranch method response: {}", response);
            return response;
        } catch (Exception ex) {
            log.error("nciGateway getDebtsListByBranch method server throws error: %s".formatted(ex.getMessage()));
            throw new TimeOut("nciGateway getDebtsListByBranch method server throws error: %s".formatted(ex.getMessage()));
        }
    }

    @Override
    public boolean getBankDayStatus(String branch) {
        try {
            var response = nciGatewayFeign.getBankDayStatus();
            assert response != null;
            Field myField = response.getClass().getDeclaredField("_" + branch);
            var result = (Boolean) myField.get(response);
            log.info("{} day status is {}", branch, result);
            return result;
        } catch (Exception ex) {
            log.error("getBankDay status method error: {}", ex.getMessage());
            return false;
        }
    }

    @Override
    public BankDayResponse getBankDayByBranch() {
        try {
            return nciGatewayFeign.getBankDayStatus();
        } catch (Exception ex) {
            throw new TimeOut("nciGateway getBankDayByBranch method server throws error: %s".formatted(ex.getMessage()));
        }
    }

    @Override
    public TransactionDto getLoanTransactions(TransactionRequest request) {
        try {
            var response = nciGatewayFeign.getLoanTransactions(request);
            log.info("getLoanTransactions response {}", Arrays.toString(response));

            return TransactionDto.builder()
                    .transactions(List.of(response))
                    .build();
        } catch (Exception ex) {
            log.error("nciGateway getLoanTransactions status method error: {}", ex.getMessage());
            throw new TimeOut("nciGateway getLoanTransactions method server throws error: %s".formatted(ex.getMessage()));
        }
    }

    private ProblemDto returnResponse(ResponseCode code) {
        return ProblemDto.builder()
                .code(code.getCode())
                .message(code.getMessage())
                .params(Map.of())
                .build();
    }

    @Override
    public GeneralResponse getTransactionById(String id) {
        try {
            return nciGatewayFeign.getTransactionById(id);
        } catch (Exception ex) {
            throw new TimeOut("nciGateway getTransactionById method server throws error: %s".formatted(ex.getMessage()));
        }
    }


}

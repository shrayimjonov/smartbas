package nci.billing.service.simulate;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nci.billing.exception.SimulateThrow;
import nci.billing.model.dto.DebtsDto;
import nci.billing.model.dto.LoanDto;
import nci.billing.model.dto.TransactionDto;
import nci.billing.model.dto.response.ResultData;
import nci.billing.model.request.TransactionRequest;
import nci.billing.model.response.*;
import nci.billing.service.InfoService;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Random;


@Component
@Slf4j
@RequiredArgsConstructor
public class InfoSimulate implements InfoService {
    private final ObjectMapper objectMapper;
    private final static String path = "resources/static/";

    @Override
    public ClientResponse getClientDataByLoanId(LoanDto dto) {
        log.info("Starting simulate getClientDataByLoanId....");
        try {
            var responseList = objectMapper.readValue(new File(path + "clientInfo.json"), ClientResponse[].class);
            var random = new Random().nextInt(7);
            log.info("random number: {}", random);
            return Arrays.stream(responseList).toList().get(random);
        } catch (Exception ex) {
            log.debug("SIMULATE getClientDataByLoanId method read file error: {}", ex.getMessage());
            throw new SimulateThrow("SIMULATE getClientDataByLoanId method read file error: " + ex.getMessage());
        }

    }

    @Override
    public LoanResponse getClientDebtInfoByLoanId(LoanDto dto) {
        log.info("Starting simulate getClientDebtInfoByLoanId....");
        try {
            var responseList = objectMapper.readValue(new File(path + "loanInfo.json"), LoanResponse[].class);
            var random = new Random().nextInt(7);
            log.info("random number: {}", random);
            return Arrays.stream(responseList).toList().get(random);
        } catch (Exception ex) {
            log.debug("SIMULATE getClientDebtInfoByLoanId method read file error: {}", ex.getMessage());
            throw new SimulateThrow("SIMULATE getClientDebtInfoByLoanId method read file error: " + ex.getMessage());
        }
    }

    @Override
    public DebtsListResponse getDebtsListByBranch(DebtsDto dto) {
        log.info("Starting simulate getDebtsListByBranch....");
        try {
            return objectMapper.readValue(new File(path + "debts.json"), DebtsListResponse.class);
        } catch (Exception ex) {
            log.debug("SIMULATE getDebtsListByBranch method read file error: {}", ex.getMessage());
            throw new SimulateThrow("SIMULATE getDebtsListByBranch method read file error: " + ex.getMessage());
        }
    }

    @Override
    public boolean getBankDayStatus(String branch) {
        return false;
    }

    @Override
    public TransactionDto getLoanTransactions(TransactionRequest request) {
        try {
            var trans = objectMapper.readValue(new File(path + "transaction.json"), TransactionResponse[].class);
            return TransactionDto.builder()
                    .transactions(List.of(trans))
                    .build();
        } catch (Exception ex) {
            log.debug("SIMULATE getLoanTransactions method read file error: {}", ex.getMessage());
            throw new SimulateThrow("SIMULATE getLoanTransactions method read file error: " + ex.getMessage());
        }
    }

    @Override
    public BankDayResponse getBankDayByBranch() {
        return null;
    }

    @Override
    public GeneralResponse getTransactionById(String id) {
        return null;
    }

}

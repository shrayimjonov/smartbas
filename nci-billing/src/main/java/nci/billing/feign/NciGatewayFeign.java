package nci.billing.feign;

import nci.billing.model.dto.response.ResultData;
import nci.billing.model.request.*;
import nci.billing.model.response.*;
import nci.billing.model.response.nci.NCIClientInfoResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "nciGateway", url = "${nci.gateway-url}", configuration = NciGatewayConfiguration.class)
public interface NciGatewayFeign {

    @PostMapping("/client/byLoan")
    ClientResponse getClientDataByLoanId(@RequestBody LoanRequest request);

    @PostMapping("/loan/isLoan")
    LoanResponse getClientDebtInfoByLoanId(@RequestBody LoanRequest request);

    @PostMapping("/loan/debts")
    DebtsListResponse getDebtsListByBranch(@RequestBody DebtsRequest request);

    @PostMapping("/loan/transactions")
    TransactionResponse[] getLoanTransactions(@RequestBody TransactionRequest request);

    @PostMapping("/loan/info")
    ResultData<LoanResponse> getLoanInfoByLoanExtId(@RequestBody LoanInfoRequest request);

    @GetMapping("/day")
    BankDayResponse getBankDayStatus();

    @GetMapping("/loan/transaction")
    GeneralResponse getTransactionById(@RequestParam("id") String id);

    @GetMapping("/api/course")
    ResultData<List<CourseResponse>> getCourse();

    @GetMapping("/card/v2/byPersonCode")
    ResultData<List<NCICardsByPersonCodeResponse>> getCardsByPersonCode(@RequestParam("personCode") String personCode);

    @PostMapping("/client/byPinfl")
    List<NCIClientInfoResponse> getClientInfoByPINFL(NCIClientInfoByPINFLRequest request);


}

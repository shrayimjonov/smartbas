package nci.billing.controller;

import jakarta.validation.Valid;
import nci.billing.model.dto.DebtsDto;
import nci.billing.model.dto.LoanDto;
import nci.billing.model.request.TransactionRequest;
import nci.billing.service.InfoService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/info")
public class InfoController {

    private final InfoService infoService;

    public InfoController(@Qualifier(value = "infoImpl") InfoService infoService) {
        this.infoService = infoService;
    }

    @PostMapping("/client")
    public ResponseEntity<?> getClientDataByLoanId(@RequestBody @Valid LoanDto dto) {
        return ResponseEntity.ok(infoService.getClientDataByLoanId(dto));
    }

    @PostMapping("/loan")
    public ResponseEntity<?> getClientDebtInfoByLoanId(@RequestBody @Valid LoanDto dto) {
        return ResponseEntity.ok(infoService.getClientDebtInfoByLoanId(dto));
    }

    @PostMapping("/debts")
    public ResponseEntity<?> getClientDebtInfoByLoanId(@RequestBody @Valid DebtsDto dto) {
        return ResponseEntity.ok(infoService.getDebtsListByBranch(dto));
    }

    @PostMapping("/transactions")
    public ResponseEntity<?> getLoanTransactions(@RequestBody @Valid TransactionRequest request) {
        return ResponseEntity.ok(infoService.getLoanTransactions(request));
    }

    @GetMapping("/day")
    public ResponseEntity<?> getBankDayStatus() {
        return ResponseEntity.ok(infoService.getBankDayByBranch());
    }

    @GetMapping("/transaction")
    public ResponseEntity<?> getTransactionById(@RequestParam("id") String id) {
        return ResponseEntity.ok(infoService.getTransactionById(id));
    }
}

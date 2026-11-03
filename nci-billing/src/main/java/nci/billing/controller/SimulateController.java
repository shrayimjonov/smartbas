package nci.billing.controller;

import jakarta.validation.Valid;
import nci.billing.model.dto.*;
import nci.billing.model.request.PayRequest;
import nci.billing.model.request.TransactionRequest;
import nci.billing.service.CoreService;
import nci.billing.service.InfoService;
import nci.billing.service.impl.FraudDetection;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/simulate")
public class SimulateController {
    private final InfoService infoService;
    private final CoreService coreService;
    private final FraudDetection fraudService;

    public SimulateController(@Qualifier(value = "infoSimulate") InfoService infoService,
                              @Qualifier(value = "coreSimulate") CoreService coreService, FraudDetection fraudService) {
        this.infoService = infoService;
        this.coreService = coreService;
        this.fraudService = fraudService;
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

    @PostMapping("/check")
    public ResponseEntity<?> checkTransaction(@RequestBody @Valid CheckPaymentDto dto) {
        return ResponseEntity.ok(coreService.checkTransaction(dto));
    }

    @PostMapping("/pay")
    public ResponseEntity<?> payTransaction(@RequestBody @Valid PayDto dto) {
        return ResponseEntity.ok(coreService.pay(dto));
    }

    @PostMapping("/reverse")
    public ResponseEntity<?> reverseTransaction(@RequestBody @Valid ReverseDto dto) {
        return ResponseEntity.ok(coreService.reverseNCITransaction(dto));
    }

    @PostMapping("/transactions")
    public ResponseEntity<?> getLoanTransactions(@RequestBody @Valid TransactionRequest request) {
        return ResponseEntity.ok(infoService.getLoanTransactions(request));
    }

    @PostMapping("/fraud")
    public ResponseEntity<?> checkFraud(@RequestBody PayRequest request) {
        fraudService.isFraudulentPayment(request);
        return ResponseEntity.ok("");
    }

}

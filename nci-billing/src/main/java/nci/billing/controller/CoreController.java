package nci.billing.controller;

import jakarta.validation.Valid;
import nci.billing.model.dto.*;
import nci.billing.service.CoreService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/core")
public class CoreController {
    private final CoreService coreService;

    public CoreController(@Qualifier(value = "coreImpl") CoreService coreService) {
        this.coreService = coreService;
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

    @PostMapping("/repay")
    public ResponseEntity<?> repayMainDebt(@RequestBody @Valid RepayMainDebtDto dto) {
        return ResponseEntity.ok(coreService.repayMainDebtByLoan(dto));
    }

    @PostMapping("/repay/v2")
    public ResponseEntity<?> repayMainDebtV2(@RequestBody @Valid RepayDTO dto) {
        return ResponseEntity.ok(coreService.repayMainDebtByLoanV2(dto));
    }

}

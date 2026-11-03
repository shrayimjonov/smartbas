package soap.humo_gateway.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import soap.humo_gateway.dto.request.*;
import soap.humo_gateway.service.PaymentService;

import javax.servlet.ServletRequest;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/payment")
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping("/byId")
    public ResponseEntity<?> getPaymentById(@RequestBody @Valid PaymentRequest request, ServletRequest servletRequest) {
        return ResponseEntity.ok(paymentService.getPaymentByPaymentId(servletRequest, request));
    }
    @PostMapping("/byExternal")
    public ResponseEntity<?> getPaymentByExternalId(@RequestBody @Valid RefNumRequest request, ServletRequest servletRequest) {
        return ResponseEntity.ok(paymentService.getPaymentByExternalId(servletRequest, request));
    }

    @PostMapping("/cancelById")
    public ResponseEntity<?> cancelActivePaymentById(@RequestBody @Valid CancelRequest request, ServletRequest servletRequest) {
        return ResponseEntity.ok(paymentService.cancelActiveById(servletRequest, request));
    }

    @PostMapping("/cancelByRefNum")
    public ResponseEntity<?> cancelActivePaymentByRefNum(@RequestBody @Valid CancelRefRequest request, ServletRequest servletRequest) {
        return ResponseEntity.ok(paymentService.cancelActiveByRefNum(servletRequest, request));
    }

    @PostMapping("/returnById")
    public ResponseEntity<?> returnFinishedPaymentById(@RequestBody @Valid ReturnRequest request, ServletRequest servletRequest) {
        return ResponseEntity.ok(paymentService.returnFinishedById(servletRequest, request));
    }

    @PostMapping("/returnByRefNum")
    public ResponseEntity<?> returnFinishedPaymentByRefNum(@RequestBody @Valid ReturnRefRequest request, ServletRequest servletRequest) {
        return ResponseEntity.ok(paymentService.returnFinishedByRefNum(servletRequest, request));
    }

    @PostMapping("/recoCreate")
    public ResponseEntity<?> createReconciliationEPos(@RequestBody @Valid RecoCreateRequest request, ServletRequest servletRequest) {
        return ResponseEntity.ok(paymentService.reconciliationCreate(servletRequest, request));
    }

    @PostMapping("/recoFinish")
    public ResponseEntity<?> finishReconciliationEPos(@RequestBody @Valid RecoFinishRequest request, ServletRequest servletRequest) {
        return ResponseEntity.ok(paymentService.reconciliationFinish(servletRequest, request));
    }
}

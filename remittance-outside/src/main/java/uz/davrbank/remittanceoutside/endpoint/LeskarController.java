/**
 * Author: Shukhratjon Rayimjonov
 * Date: 12.11.2025
 * Time: 12:31
 * Project: remittance-outside
 */
package uz.davrbank.remittanceoutside.endpoint;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import uz.davrbank.remittanceoutside.utils.constants.ApiConstants;
import uz.davrbank.remittanceoutside.aggregate.model.ResultData;
import uz.davrbank.remittanceoutside.aggregate.dto.leskarintegration.*;
import uz.davrbank.remittanceoutside.aggregate.dto.leskarintegration.base.LeskarBaseResponse;
import uz.davrbank.remittanceoutside.service.LeskarService;

@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@RequestMapping(ApiConstants.leskarIntegrationTransferRootApi)
public class LeskarController {

    LeskarService leskarService;

    @PostMapping("/countries")
    public ResponseEntity<ResultData<LeskarBaseResponse<LeskarGetCountryListResponse>>> getCountryList() {
        return ResponseEntity.ok(leskarService.getCountryList());
    }

    @PostMapping("/fees")
    public ResponseEntity<ResultData<LeskarBaseResponse<LeskarGetFeeResponse>>> getFeeList(@RequestBody LeskarGetFeeRequest request) {
        return ResponseEntity.ok(leskarService.getFees(request));
    }

    @PostMapping("/person/create")
    public ResponseEntity<ResultData<LeskarBaseResponse<LeskarCreatePersonResponse>>> createPerson(@RequestBody LeskarCreatePersonRequest request) {
        return ResponseEntity.ok(leskarService.createPerson(request));
    }

    @PostMapping("/create")
    public ResponseEntity<ResultData<LeskarBaseResponse<LeskarCreateTransferResponse>>> createTransfer(@RequestBody LeskarCreateTransferRequest request) {
        return ResponseEntity.ok(leskarService.createTransfer(request));
    }

    @PostMapping("/confirm")
    public ResponseEntity<ResultData<LeskarBaseResponse<LeskarConfirmTransferResponse>>> confirmTransfer(@RequestBody LeskarConfirmTransferRequest request) {
        return ResponseEntity.ok(leskarService.confirmTransfer(request));
    }

    @PostMapping("/cancel")
    public ResponseEntity<ResultData<LeskarBaseResponse<LeskarConfirmTransferResponse>>> cancelTransfer(@RequestBody LeskarCancelTransferRequest request) {
        return ResponseEntity.ok(leskarService.cancelTransfer(request));
    }

    @PostMapping("/person/info")
    public ResponseEntity<ResultData<LeskarBaseResponse<LeskarPersonInfoResponse>>> personInfo(@RequestBody LeskarPersonInfoRequest request) {
        return ResponseEntity.ok(leskarService.personInfo(request));
    }

    @PostMapping("/payout")
    public ResponseEntity<ResultData<LeskarBaseResponse<LeskarConfirmTransferResponse>>> payout(@RequestBody LeskarPayoutRequest request) {
        return ResponseEntity.ok(leskarService.payout(request));
    }

    @PostMapping("/payout/confirm")
    public ResponseEntity<ResultData<LeskarBaseResponse<LeskarConfirmTransferResponse>>> payoutConfirm(@RequestBody LeskarPayoutConfirmRequest request) {
        return ResponseEntity.ok(leskarService.payoutConfirm(request));
    }

    @PostMapping("/status")
    public ResponseEntity<ResultData<LeskarBaseResponse<LeskarConfirmTransferResponse>>> transferStatus(@RequestBody LeskarTransferStatusRequest request) {
        return ResponseEntity.ok(leskarService.transferStatus(request));
    }

    @PostMapping("/info")
    public ResponseEntity<ResultData<LeskarBaseResponse<LeskarTransferInfoResponse>>> transferInfo(@RequestBody LeskarTransferInfoRequest request) {
        return ResponseEntity.ok(leskarService.transferInfo(request));
    }
}

/**
 * Author: Shukhratjon Rayimjonov
 * Date: 12.11.2025
 * Time: 12:31
 * Project: remittance-outside
 */
package uz.davrbank.remittanceoutside.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import uz.davrbank.remittanceoutside.aggregate.model.ResultData;
import uz.davrbank.remittanceoutside.aggregate.dto.leskarintegration.*;
import uz.davrbank.remittanceoutside.aggregate.dto.leskarintegration.base.LeskarBaseResponse;
import uz.davrbank.remittanceoutside.aggregate.enums.IntegrationType;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LeskarService {

    IntegrationService integrationService;

    static String leskarTransferBasePath = "/v1/transfer";

    public ResultData<LeskarBaseResponse<LeskarGetCountryListResponse>> getCountryList() {
        return ResultData.ok(integrationService.post(IntegrationType.LESKAR,
                leskarTransferBasePath + "/countries", null, null, new ParameterizedTypeReference<>() {
        }, null));
    }

    public ResultData<LeskarBaseResponse<LeskarGetFeeResponse>> getFees(LeskarGetFeeRequest request) {
        return ResultData.ok(integrationService.post(IntegrationType.LESKAR, leskarTransferBasePath + "/fees", request, null, new ParameterizedTypeReference<>() {
        }, null));
    }

    public ResultData<LeskarBaseResponse<LeskarCreatePersonResponse>> createPerson(LeskarCreatePersonRequest request) {
        return ResultData.ok(integrationService.postEncrypted(IntegrationType.LESKAR, leskarTransferBasePath + "/person/create", request, null, new ParameterizedTypeReference<>() {
        }, null));
    }

    public ResultData<LeskarBaseResponse<LeskarCreateTransferResponse>> createTransfer(LeskarCreateTransferRequest request) {
        return ResultData.ok(integrationService.post(IntegrationType.LESKAR, leskarTransferBasePath + "/create", request, null, new ParameterizedTypeReference<>() {
        }, null));
    }

    public ResultData<LeskarBaseResponse<LeskarConfirmTransferResponse>> confirmTransfer(LeskarConfirmTransferRequest request) {
        return ResultData.ok(integrationService.post(IntegrationType.LESKAR, leskarTransferBasePath + "/confirm", request, null, new ParameterizedTypeReference<>() {
        }, null));
    }

    public ResultData<LeskarBaseResponse<LeskarConfirmTransferResponse>> cancelTransfer(LeskarCancelTransferRequest request) {
        return ResultData.ok(integrationService.post(IntegrationType.LESKAR, leskarTransferBasePath + "/cancel", request, null, new ParameterizedTypeReference<>() {
        }, null));
    }

    public ResultData<LeskarBaseResponse<LeskarPersonInfoResponse>> personInfo(LeskarPersonInfoRequest request) {
        return ResultData.ok(integrationService.post(IntegrationType.LESKAR, leskarTransferBasePath + "/person/info", request, null, new ParameterizedTypeReference<>() {
        }, null));
    }

    public ResultData<LeskarBaseResponse<LeskarConfirmTransferResponse>> payout(LeskarPayoutRequest request) {
        return ResultData.ok(integrationService.post(IntegrationType.LESKAR, leskarTransferBasePath + "/payout", request, null, new ParameterizedTypeReference<>() {
        }, null));
    }

    public ResultData<LeskarBaseResponse<LeskarConfirmTransferResponse>> payoutConfirm(LeskarPayoutConfirmRequest request) {
        return ResultData.ok(integrationService.post(IntegrationType.LESKAR, leskarTransferBasePath + "/payout/confirm", request, null, new ParameterizedTypeReference<>() {
        }, null));
    }

    public ResultData<LeskarBaseResponse<LeskarConfirmTransferResponse>> transferStatus(LeskarTransferStatusRequest request) {
        return ResultData.ok(integrationService.post(IntegrationType.LESKAR, leskarTransferBasePath + "/status", request, null, new ParameterizedTypeReference<>() {
        }, null));
    }

    public ResultData<LeskarBaseResponse<LeskarTransferInfoResponse>> transferInfo(LeskarTransferInfoRequest request) {
        return ResultData.ok(integrationService.post(IntegrationType.LESKAR, leskarTransferBasePath + "/info", request, null, new ParameterizedTypeReference<>() {
        }, null));
    }
}

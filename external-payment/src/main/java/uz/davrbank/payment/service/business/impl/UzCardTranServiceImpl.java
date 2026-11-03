package uz.davrbank.payment.service.business.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uz.davrbank.payment.model.dto.tran.request.TranBaseRequestDTO;
import uz.davrbank.payment.model.dto.tran.response.TranBaseResponseDTO;
import uz.davrbank.payment.model.dto.uzcard.UzCardBaseRequest;
import uz.davrbank.payment.model.dto.uzcard.request.UzCardUniversalCreditRequest;
import uz.davrbank.payment.model.dto.uzcard.request.params.TransPayPurposeParam;
import uz.davrbank.payment.model.dto.uzcard.response.result.UzCardBasePaymentResult;
import uz.davrbank.payment.model.enums.UzCardStatus;
import uz.davrbank.payment.service.business.UzCardTranService;
import uz.davrbank.payment.service.web.UzCardIntegrationService;
import uz.nodir.commondto.model.dto.core.response.SimpleResponse;
import uz.nodir.commondto.model.enums.ResultState;
import uz.nodir.commondto.utils.DateUtils;

@Service
@Slf4j
@RequiredArgsConstructor
public class UzCardTranServiceImpl implements UzCardTranService {
    private final UzCardIntegrationService uzCardIntegrationService;
    private final static String UZCARD_DEFAULT_TYPE = "payment";
    private final static String UZCARD_CCY = "860";

    @Override
    public SimpleResponse<TranBaseResponseDTO> credit(TranBaseRequestDTO requestDTO) {
        log.info("Begin credit transaction from request: {}", requestDTO);

        var birthdayResult = DateUtils.LocalDateConvertToFormat(
                requestDTO.getPersonParam().getBirthDate(),
                "dd.MM.yyyy"
        );

        if (!birthdayResult.getStatus())
            return new SimpleResponse<>(birthdayResult.getError().getCode(), birthdayResult.getError().getMessage());

        var expiryDateResult = DateUtils.LocalDateConvertToFormat(
                requestDTO.getPersonParam().getPassportExpiryDate(),
                "dd.MM.yyyy"
        );

        if (!expiryDateResult.getStatus())
            return new SimpleResponse<>(expiryDateResult.getError().getCode(), expiryDateResult.getError().getMessage());

        var request =
                new UzCardBaseRequest<>(
                        "dv_external_" + System.currentTimeMillis(),
                        "2.0",
                        "p2p.universal.credit",
                        new UzCardUniversalCreditRequest(
                                new UzCardUniversalCreditRequest.UniversalCredit(
                                        requestDTO.getExtId(),
                                        requestDTO.getAmount(),
                                        requestDTO.getMerchantId(),
                                        requestDTO.getTerminalId(),
                                        requestDTO.getCreditParams().getAccount(),
                                        new UzCardUniversalCreditRequest.UniversalCreditSender(
                                                "davr_external_system",
                                                "DAVR BANK",
                                                requestDTO.getPersonParam().getFirstName(),
                                                requestDTO.getPersonParam().getSurname(),
                                                requestDTO.getPersonParam().getPatronymic(),
                                                requestDTO.getDebitParams().getAccount(),
                                                requestDTO.getDebitParams().getRefNum(),
                                                new UzCardUniversalCreditRequest.UniversalCreditDoc(
                                                        requestDTO.getPersonParam().getNationality().getCode(),
                                                        "passport",
                                                        requestDTO.getPersonParam().getPassportSerial() + requestDTO.getPersonParam().getPassportNumber(),
                                                        birthdayResult.getData(),
                                                        expiryDateResult.getData(),
                                                        requestDTO.getPersonParam().getPersonCode()
                                                )
                                        )
                                )
                        ));

        var creditResponse = uzCardIntegrationService.svGateConnect(request);

        if (!creditResponse.getStatus()) {
            if (creditResponse.isTimeOut())
                return new SimpleResponse<>(creditResponse.getError().getMessage(), true);
            else
                return new SimpleResponse<>(creditResponse.getError().getCode(), creditResponse.getError().getMessage());
        }

        if (creditResponse.getData().getError() != null)
            return new SimpleResponse<>(creditResponse.getData().getError().getCode(), creditResponse.getData().getError().getMessage());

        var result = (UzCardBasePaymentResult) creditResponse.getData().getResult();

        if (result.getResp() != 0 && !result.getStatus().equals(UzCardStatus.OK))
            return new SimpleResponse<>(result.getResp(), ResultState.FailedResponse.getMessage());

        var response = new TranBaseResponseDTO(
                result,
                request.toMap(),
                creditResponse.toMap()
        );

        log.info("Exit credit transaction with result: {}", response);
        return new SimpleResponse<>(
                response
        );
    }


    @Override
    public SimpleResponse<TranBaseResponseDTO> debit(TranBaseRequestDTO requestDTO) {
        log.info("Begin debit transaction from request: {}", requestDTO);


        var request =
                new UzCardBaseRequest<>(
                        "dv_external_" + System.currentTimeMillis(),
                        "2.0",
                        "trans.pay.purpose",
                        new TransPayPurposeParam(
                                new TransPayPurposeParam.TransPayPurposeTran(
                                        UZCARD_DEFAULT_TYPE,
                                        requestDTO.getCreditParams().getAccount(),
                                        requestDTO.getDebitParams().getAccount(),
                                        requestDTO.getAmount(),
                                        0,
                                        UZCARD_CCY,
                                        requestDTO.getExtId(),
                                        requestDTO.getMerchantId(),
                                        requestDTO.getTerminalId()
                                )
                        )
                );

        var debitResponse = uzCardIntegrationService.pay(request);

        if (!debitResponse.getStatus()) {
            if (debitResponse.isTimeOut())
                return new SimpleResponse<>(debitResponse.getError().getMessage(), true);
            else
                return new SimpleResponse<>(debitResponse.getError().getCode(), debitResponse.getError().getMessage());
        }

        if (debitResponse.getData().getError() != null)
            return new SimpleResponse<>(debitResponse.getData().getError().getCode(), debitResponse.getData().getError().getMessage());

        var result = debitResponse.getData().getResult();

        if (result.getResp() != 0 && !result.getStatus().equals(UzCardStatus.OK))
            return new SimpleResponse<>(result.getResp(), ResultState.FailedResponse.getMessage());

        var response = new TranBaseResponseDTO(
                result,
                request.toMap(),
                debitResponse.toMap()
        );

        log.info("Exit debit transaction with result: {}", response);
        return new SimpleResponse<>(
                response
        );
    }
}

package uz.davrbank.payment.service.business.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uz.davrbank.payment.model.dto.huMo.request.HuMoCreditRequest;
import uz.davrbank.payment.model.dto.huMo.request.HuMoDebitRequest;
import uz.davrbank.payment.model.dto.tran.request.TranBaseRequestDTO;
import uz.davrbank.payment.model.dto.tran.response.TranBaseResponseDTO;
import uz.davrbank.payment.service.business.HuMoTranService;
import uz.davrbank.payment.service.web.HuMoGatewayService;
import uz.davrbank.payment.utils.CommonUtils;
import uz.nodir.commondto.model.dto.core.response.SimpleResponse;
import uz.nodir.commondto.utils.DateUtils;

@Service
@Slf4j
@RequiredArgsConstructor
public class HuMoTranServiceImpl implements HuMoTranService {
    private final HuMoGatewayService huMoGatewayService;

    @Override
    public SimpleResponse<TranBaseResponseDTO> credit(TranBaseRequestDTO requestDTO) {
        log.info("Begin credit transaction from request: {}", requestDTO);

        var birthdayResult = DateUtils.LocalDateConvertToFormat(
                requestDTO.getPersonParam().getBirthDate(),
                "yyyy-MM-dd"
        );

        if (!birthdayResult.getStatus())
            return new SimpleResponse<>(birthdayResult.getError().getCode(), birthdayResult.getError().getMessage());

        var expiryDateResult = DateUtils.LocalDateConvertToFormat(
                requestDTO.getPersonParam().getPassportExpiryDate(),
                "yyyy-MM-dd"
        );

        if (!expiryDateResult.getStatus())
            return new SimpleResponse<>(expiryDateResult.getError().getCode(), expiryDateResult.getError().getMessage());


        var creditRequest = new HuMoCreditRequest(
                requestDTO.getExtId(),
                requestDTO.getCreditParams().getAccount(),
                requestDTO.getCreditParams().getExpiry(),
                requestDTO.getAmount(),
                requestDTO.getMerchantId(),
                requestDTO.getTerminalId(),
                CommonUtils.huMoGateway_username,
                CommonUtils.huMoGateway_SUBSYTEM,
                new HuMoCreditRequest.Params(
                        "001", //passport
                        requestDTO.getPersonParam().getPassportSerial(),
                        requestDTO.getPersonParam().getPassportNumber(),
                        requestDTO.getPersonParam().getSurname(),
                        requestDTO.getPersonParam().getFirstName(),
                        requestDTO.getPersonParam().getPatronymic(),
                        requestDTO.getPersonParam().getNationality().name(),
                        expiryDateResult.getData(),
                        requestDTO.getPersonParam().getPersonCode(),
                        birthdayResult.getData()
                )
        );

        var creditResponse = huMoGatewayService.credit(creditRequest);


        if (!creditResponse.getStatus()) {
            if (creditResponse.isTimeOut())
                return new SimpleResponse<>(creditResponse.getError().getMessage(), true);
            else
                return new SimpleResponse<>(creditResponse.getError().getCode(), creditResponse.getError().getMessage());
        }

        if (creditResponse.getData().getError() != null)
            return new SimpleResponse<>(creditResponse.getData().getError().getCode(), creditResponse.getData().getError().getMessage());

        var result = creditResponse.getData();


        var response = new TranBaseResponseDTO(
                result,
                creditRequest.toMap(),
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

        var debitRequest = new HuMoDebitRequest(
                requestDTO.getExtId(),
                requestDTO.getDebitParams().getAccount(),
                requestDTO.getDebitParams().getExpiry(),
                requestDTO.getAmount(),
                requestDTO.getMerchantId(),
                requestDTO.getTerminalId(),
                CommonUtils.huMoGateway_username,
                CommonUtils.huMoGateway_SUBSYTEM
        );

        var debitResponse = huMoGatewayService.debit(debitRequest);


        if (!debitResponse.getStatus()) {
            if (debitResponse.isTimeOut())
                return new SimpleResponse<>(debitResponse.getError().getMessage(), true);
            else
                return new SimpleResponse<>(debitResponse.getError().getCode(), debitResponse.getError().getMessage());
        }

        if (debitResponse.getData().getError() != null)
            return new SimpleResponse<>(debitResponse.getData().getError().getCode(), debitResponse.getData().getError().getMessage());

        var result = debitResponse.getData();


        var response = new TranBaseResponseDTO(
                result,
                debitRequest.toMap(),
                debitResponse.toMap()
        );

        log.info("Exit debit transaction with result: {}", response);
        return new SimpleResponse<>(
                response
        );
    }
}

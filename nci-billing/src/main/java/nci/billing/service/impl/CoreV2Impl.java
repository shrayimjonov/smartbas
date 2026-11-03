package nci.billing.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nci.billing.feign.LoanFeign;
import nci.billing.model.dto.PayDtoV2;
import nci.billing.model.dto.ProblemDto;
import nci.billing.model.dto.response.ResultData;
import nci.billing.model.enums.ResponseCode;
import nci.billing.model.request.loan.PaymentRequest;
import nci.billing.service.CoreV2Service;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CoreV2Impl implements CoreV2Service {
    private final LoanFeign loanFeign;

    @Override
    public ResultData<?> pay(PayDtoV2 dto) {
        log.info("Received request to pay: {}", dto);
        var request = requestBuilder(dto);

        try {
            var response = loanFeign.doPayment(request);
            log.info("Loan Pay Response: {}", response);

            if (response.getStatus() == 99) {
                return new ResultData<>(ProblemDto.builder()
                        .code(ResponseCode.PENDING.getCode())
                        .message(ResponseCode.PENDING.getMessage())
                        .detail(response.getResult().getPaymentId())
                        .build());
            } else {
                return new ResultData<>(ResponseCode.WRONG_REQUEST, response.getDetails());
            }

        } catch (Exception e) {
            log.error("Pay response is not 2xx successful with error: {}", e.getMessage());
            return new ResultData<>(500, e.getMessage());
        }


    }


    private static PaymentRequest requestBuilder(PayDtoV2 dto) {
        var branch = dto.getLoanId().substring(0, 5);
        var loanId = Integer.valueOf(dto.getLoanId().substring(5));
        var cardNumber = dto.getCardNum() != null ? dto.getCardNum() : "****************";

        var terminal = new PaymentRequest.Terminal(dto.getTerminalId(), dto.getMerchantId(), dto.getAccount());

        var request = PaymentRequest.builder()
                .branch(branch)
                .formId(loanId)
                .amount(dto.getAmount())
                .cardNum(cardNumber)
                .paymentId(dto.getPaymentId())
                .refId(dto.getRefNum())
                .terminal(terminal)
                .originator(dto.getSystemType().getNumber())
                .build();

        log.info("""
                        {
                            "paymentId": "{}",
                            "branch": "{}",
                            "formId": {},
                            "cardNum": "{}",
                            "refId": "{}",
                            "terminal": {
                                "terminalId": "{}",
                                "merchantId": "{}",
                                "account": "{}"
                            },
                            "amount": {},
                            "originator": {}
                        }""",
                request.getPaymentId(), request.getBranch(), request.getFormId(),
                request.getCardNum(), request.getRefId(), request.getTerminal().getTerminalId(),
                request.getTerminal().getMerchantId(), request.getTerminal().getAccount(),
                request.getAmount(), request.getOriginator());
        return request;
    }


    @Override
    public ResultData<?> check(String paymentId) {
        log.info("Sending check request to paymentId: {}", paymentId);

        var checkResponse = loanFeign.getPaymentStatus(paymentId);

        log.info("Payment status response: {}", checkResponse);

        return switch (checkResponse.getStatus()) {
            case 0 -> new ResultData<>(ProblemDto.builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .message(ResponseCode.SUCCESS.getMessage())
                    .detail(checkResponse.getResult().getDocId())
                    .build());
            // -1 internal error, please check again
            case 99, -1 -> new ResultData<>(ProblemDto.builder()
                    .code(ResponseCode.PENDING.getCode())
                    .message(ResponseCode.PENDING.getMessage())
                   // .detail(checkResponse.getResult().getPaymentId())
                    .build());
            case 404 -> new ResultData<>(checkResponse.getStatus(), checkResponse.getDetails());
            default ->
                    new ResultData<>(ResponseCode.WRONG_REQUEST, checkResponse.getDetails().length() > 127 ? checkResponse.getDetails().substring(0, 127) : checkResponse.getDetails());
        };


    }
}

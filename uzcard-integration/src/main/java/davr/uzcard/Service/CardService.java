package davr.uzcard.Service;

import davr.uzcard.Model.*;
import davr.uzcard.Model.Request.*;
import davr.uzcard.Service.Feign.FeignService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CardService {
    private final FeignService feignService;
    @Value("${uzcard.card-block.terminal_id}")
    private String terminalId;
    @Value("${uzcard.card-block.merchant_id}")
    private String merchantId;

    @Value("${uzcard.pin.merchantId}")
    private String pinMerchantId;
    @Value("${uzcard.pin.terminalId}")
    private String pinTerminalId;

    public Object getCardLimitList() {
        return feignService.svgateConnect(CardLimit
                .builder()
                .build());
    }

    public Object addLimitToCard(LimitAddDto dto) {
        return feignService.svgateConnect(CardAddLimit.builder()
                .params(CardAddLimit.Params.builder()
                        .limitInfo(CardAddLimit.LimitInfo.builder()
                                .cardId(dto.getToken())
                                .limits(dto.getLimits())
                                .build())
                        .build())
                .build());
    }

    public Object getCardLimit(LimitInfoDto dto) {
        return feignService.svgateConnect(CardGetLimit.builder()
                .params(CardGetLimit.Params.builder()
                        .cardId(dto.getToken())
                        .build())
                .build());
    }

    public Object removeLimitFromCard(LimitRemoveDto dto) {
        return feignService.svgateConnect(CardRemoveLimit.builder()
                .params(CardRemoveLimit.Params.builder()
                        .limitInfo(CardRemoveLimit.LimitInfo.builder()
                                .cardId(dto.getToken())
                                .limits(dto.getLimitCodes())
                                .build())
                        .build())
                .build());
    }

    public Object cardsNewOTP(CardsNewDto dto) {
        return feignService.svgateEightConnect(CardsNewOtp.builder()
                .params(CardsNewOtp.Params.builder()
                        .card(CardsNewOtp.Card.builder()
                                .pan(dto.getCardPan())
                                .expiry(dto.getExpiry())
                                .sms(CardsNewOtp.Sms.builder()
                                        .hash("xRnlmGRKKMv")
                                        .serviceName("Davr Mobile")
                                        .build())
                                .requestorPhone(dto.getPhone())
                                .build()
                        )
                        .build())
                .build());
    }

    public Object cardsNewVerify(CardsNewVerifyDto dto) {
        return feignService.svgateEightConnect(CardsNewVerify.builder()
                .params(CardsNewVerify.Params.builder()
                        .otp(CardsNewVerify.Otp.builder()
                                .id(dto.getId())
                                .code(dto.getCode())
                                .build())
                        .build())
                .build());
    }

    public Object checkPinfl(CheckPinflDTO dto) {
        log.info("Check card to pinfl method received request: {}", dto);
        return feignService.svgateConnect(
                new BaseRequest<>("card.check.pinfl", CheckPinflParams.builder()
                        .cardinfo(
                                new CheckPinflParams.Cardinfo(
                                        dto.getToken(), dto.getPersonCode()
                                )
                        )
                        .build())
        );
    }

    public Object getCardConnectedApps(TokenListDTO dto) {
        log.info("Get token list by: {}", dto.getToken());
        return feignService.svgateConnect(
                new BaseRequest<>("token.list",
                        new TokenList.Params(
                                dto.getToken()
                        )
                )
        );
    }

    public Object pinChange(PinChangeDTO dto) {
        return feignService.svgateEightConnect(
                new BaseRequest<>("online.pin.change",
                        new PinChangeParams(
                                new PinChangeParams.PinChangeCardInfo(
                                        dto.getToken(),
                                        pinMerchantId,
                                        pinTerminalId,
                                        "davr_" + System.currentTimeMillis()
                                )
                        )
                )
        );
    }

    public Object pinSet(PinSetDTO dto) {
        log.info("Received pin set request with request: {}", dto);
        BaseRequest<PinSetParams> request = new BaseRequest<>("online.pin.set",
                new PinSetParams(
                        new PinSetParams.PinSetCardInfo(
                                dto.getToken(),
                                pinMerchantId,
                                pinTerminalId,
                                "davr_" + System.currentTimeMillis(),
                                dto.getPreset()
                        )
                )
        );
        log.info("Sending pin set request to uzCard: {}", request);
        Object response = feignService.svgateEightConnect(
                request
        );

        log.info("Finished pin set method with response: {}", response);
        return response;
    }


    public Object cashOut(CashOutDTO dto) {
        return feignService.svgateConnect(
                new BaseRequest<>("cash.out.get.code",
                        new CashOutGetCode(
                                new CashOutGetCode.CashOutTran(
                                        dto.getAmount(),
                                        dto.getToken(),
                                        dto.getExtId()
                                )
                        )
                )
        );
    }


    public Object getCVV2(CardGetCVVRequestDTO dto) {
        return feignService.svgateConnect(
                new BaseRequest<>("card.get.cvv2",
                        new CardCVVParam(
                               dto.getToken()
                        )
                )
        );
    }
}

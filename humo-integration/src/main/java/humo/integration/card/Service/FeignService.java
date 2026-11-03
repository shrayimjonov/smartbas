package humo.integration.card.Service;

import feign.FeignException;
import humo.integration.card.Exception.*;
import humo.integration.card.Feign.HumoFeign.*;
import humo.integration.card.Model.CheckCardToPinflRequestDTO;
import humo.integration.card.Model.Dto.*;
import humo.integration.card.Model.Request.*;
import humo.integration.card.Model.Request.v2.HuMoCardBaseRequest;
import humo.integration.card.Model.Request.v2.params.HuMoCardCheckToPinflParams;
import humo.integration.card.Model.Response.*;
import humo.integration.card.Model.enums.BlockType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class FeignService implements FeignServiceImpl {
    private final HumoFeign humoFeign;
    private final NciFeign nciFeign;
    private final QueryTransFeign transFeign;
    private final HumoCardFeign cardFeign;
    private final BankFeign bankFeign;
    private final TelegramExFeign telegramExFeign;

    @Value("${bank.card}")
    private String bankCard;

    @Override
    public <E> Object createVirtualCard(HumoVirtualDto dto) {
        try {
            log.debug("request to creating virtual card : {}", dto);

            var response = nciFeign.createVirtualCard(HumoVirtualReq.builder()
                    .branch(dto.getBranch())
                    .clientId(dto.getClientId())
                    .cardType("66")
                    .user("mobile")
                    .phone(dto.getPhone())
                    .account(dto.getAccount())
                    .build());
            log.debug("response from creating virtual card: {} ", response);
            return response;
        } catch (Exception ex) {
            log.error("Creating new virtual card throws exception: {}", ex.getMessage());
            throw new HumoFeignError(ex.getMessage());
        }
    }

    @Override
    public <E> Object onSmsInfo(E value) {
        log.debug("on Sms Info request : {}", value);
        try {
            var response = nciFeign.onSmsInfo(value);
            log.debug("response from on Info Sms : {}", response);
            return response;
        } catch (Exception ex) {
            log.error("on Sms Info throws exception: {}", ex.getMessage());
            throw new HumoFeignError(ex.getMessage());
        }

    }

    @Override
    public Object getClientCards(HumoVirtualCardsDto dto) {
        log.debug("getClientCards request : {}", dto);
        try {
            var response = nciFeign.getClientCards(dto.getBranch(), dto.getClientId());
            log.debug("response from getClientCards: {}", response);
            return response;
        } catch (Exception ex) {
            log.error("getClientCards exception: {}", ex.getMessage());
            throw new HumoFeignError(ex.getMessage());
        }
    }

    @Override
    public CustomerListResp findCustomerCardsByPhone(HumoPhoneDto humoPhoneDto) {
        var customerParam = CustomerListParam.builder()
                .bankId("MB_STD")
                .phone(humoPhoneDto.getPhone()).build();
        var customerListReq = CustomerListReq.builder()
                .id("DV_" + System.currentTimeMillis())
                .params(customerParam).build();
        try {
            return humoFeign.getCustomerCardsByPhone(customerListReq);
        } catch (Exception ex) {
            log.error("findCustomerCardsByPhone exception: {}", ex.getMessage());
            throw new HumoFeignError("findCustomerCardsByPhone " + ex.getMessage());
        }

    }

    @Override
    public IacsCardV3Resp findCustomerInfo(HumoCardDto humoCardDto) {
        var iacs = IacsCardV3Req.builder().params(IacsCardV3Req.IacsParams.builder().primaryAccountNumber(humoCardDto.getCardNumber()).mb_flag(1).build()).build();
        log.debug("findCustomerInfo method request: {}", iacs);
        try {
            IacsCardV3Resp iacsV3Resp = humoFeign.getCustomerCardInfo(iacs);

            log.debug("findCustomerInfo method response: {}", iacsV3Resp);
            return iacsV3Resp;
        } catch (FeignException f) {
            log.error("IIACards v3 throws error: {}", f.getMessage());
            throw new HumoFeignError(f.getMessage());
        }

    }


    @Override
    public Object findCustomerP2PInfo(HumoCardDto humoCardDto) {
        var iacs = IacsCardV3Req
                .builder()
                .params(IacsCardV3Req.IacsParams.builder()
                        .primaryAccountNumber(humoCardDto.getCardNumber()).mb_flag(1).build()).build();
        log.debug("findCustomerP2PInfo method request: {}", iacs);
        try {
            var iacsV3Resp = humoFeign.getCustomerP2PInfo(iacs);

            log.debug("findCustomerP2PInfo method response: {}", iacsV3Resp);
            return iacsV3Resp;
        } catch (FeignException f) {
            throw new HumoFeignError(f.getMessage());
        }

    }

    @Override
    public QueryTransHistResp findCardHistory(QueryTransHistoryReq req) {
        return transFeign.getCardHistoryByDate(req);
    }

    @Override
    public CardMaskedPanResp findCardToken(String humoPan) {
        log.debug("Humo Token request: {}", humoPan);
        try {
            var token = cardFeign.getHumoToken(humoPan);

            log.debug("Humo Token response: {}", token);
            return token;
        } catch (FeignException f) {
            throw new HumoFeignError(f.getMessage());
        }

    }

    @Override
    public CardMaskedPanResp findCardToken2(String humoPan) {
        log.debug("Humo Token request: {}", humoPan);
        var realPan = MaskedPanReq.builder().id("12345").params(MaskedPanReq.Params.builder().pan(humoPan).build()).build();
        try {
            var cardMaskedPanResp = CardMaskedPanResp.builder().masked_pan(cardFeign.getHumoMaskedPan(realPan).getResult().getCard()).build();

            log.debug("Humo Token response: {}", cardMaskedPanResp);
            return cardMaskedPanResp;
        } catch (FeignException f) {
            throw new HumoFeignError(f.getMessage());
        }
    }

    @Override
    public CardRealPanResp findCardRealPan(String humoToken) {
        log.debug("Humo Real Pan request: {}", humoToken);
        try {
            var realPan = cardFeign.getHumoRealPan(humoToken);

            log.debug("Humo Real Pan response: {}", realPan);
            return realPan;
        } catch (FeignException f) {
            throw new HumoFeignError(f.getMessage());
        }
    }

    @Override
    public Object cardActivateSms(Object request) {
        log.debug("Humo cardActivateSms request: {}", request);
        try {
            var response = humoFeign.cardPhoneSmsOn(request);
            log.debug("Humo cardActivateSms response: {}", response);
            return response;
        } catch (Exception e) {
            throw new HandleHumoError(e.getMessage());
        }
    }

    @Override
    public Object customerDeactivateSms(Object request) {
        log.debug("Humo customerDeactivateSms request: {}", request);
        try {
            var response = humoFeign.customerCardsSmsOf(request);
            log.debug("Humo customerDeactivateSms response: {}", response);
            return response;
        } catch (Exception e) {
            throw new HandleHumoError(e.getMessage());
        }
    }


    @Override
    public CardRealPanResp findCardRealPan2(String maskedPan) {
        log.debug("Humo Real Pan request: {}", maskedPan);
        var token = RealPanReq.builder().id("12345").params(RealPanReq.Params.builder().card(maskedPan).build()).build();
        try {
            var realCard = CardRealPanResp.builder().pan(cardFeign.getHumoPan(token).getResult().getPan()).build();
            log.debug("Humo Real Pan response: {}", realCard);
            return realCard;
        } catch (FeignException f) {
            throw new HumoFeignError(f.getMessage());
        }

    }


    @Override
    public Object blockOrUnBlockCard(CardBlockDto dto) {

        var request = CardBlockRequest.builder()
                .cardNumber(dto.getCard())
                .cause(dto.getCause())
                .build();

        if (dto.getType() == BlockType.BLOCK)
            return nciFeign.blockVirtualCard(request);
        else if (dto.getType() == BlockType.UNBLOCK)
            return nciFeign.unBlockVirtualCard(request);
        else
            throw new BadRequestException("Type not found");


    }


    @Override
    public Object getBinList() {
        log.debug("getBinList method request");
        try {
            var response = humoFeign.getBinList();

            log.debug("getBinList method response: {}", response);
            return response;
        } catch (FeignException f) {
            throw new HumoFeignError(f.getMessage());
        }
    }

    @Override
    public Object getCardHistory(HumoHistoryRequestDTO requestDTO) {
        log.debug("get card history method request: {}", requestDTO);
        try {
            var request = new HistoryMiddlewareRequest(
                    requestDTO
            );

            return humoFeign.getCardHistory(request);
        } catch (FeignException f) {
            throw new HumoFeignError(f.getMessage());
        }
    }

    @Override
    public Object tranSortedScoring(Object request) {
        log.debug("tran sorted scoring method request: {}", request);
        try {
            return humoFeign.tranSortedScoring(request);
        } catch (FeignException | NotFoundException f) {
            throw new ScoringException(f.getMessage());
        }
    }


    @Override
    public Object scoringByPersonCode(Object request) {
        log.debug("scoring by person code method request: {}", request);
        try {
            return humoFeign.scoringByPersonCode(request);
        } catch (FeignException | NotFoundException f) {
            throw new ScoringException(f.getMessage());
        }
    }

    @Override
    public Object checkCard2PINFL(CheckCardToPinflRequestDTO request) {
        log.debug("check card to pinfl request: {}", request);

        var huMoRequest = new HuMoCardBaseRequest<>(
                "humo_integration_" + System.currentTimeMillis(),
                new HuMoCardCheckToPinflParams(
                        request
                )
        );
        try {
            return humoFeign.checkCard2PINFL(huMoRequest);
        } catch (FeignException | NotFoundException f) {
            throw new ScoringException(f.getMessage());
        }
    }


}

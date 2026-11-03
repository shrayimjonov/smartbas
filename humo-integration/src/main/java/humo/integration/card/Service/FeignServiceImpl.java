package humo.integration.card.Service;

import humo.integration.card.Model.CheckCardToPinflRequestDTO;
import humo.integration.card.Model.Dto.*;
import humo.integration.card.Model.Request.QueryTransHistoryReq;
import humo.integration.card.Model.Response.*;

public interface FeignServiceImpl {
    CardRealPanResp findCardRealPan2(String maskedPan);

    public CardMaskedPanResp findCardToken2(String humoPan);

    CustomerListResp findCustomerCardsByPhone(HumoPhoneDto humoPhoneDto);

    IacsCardV3Resp findCustomerInfo(HumoCardDto humoCardDto);

    QueryTransHistResp findCardHistory(QueryTransHistoryReq req);

    CardMaskedPanResp findCardToken(String humoPan);

    CardRealPanResp findCardRealPan(String humoToken);

    Object cardActivateSms(Object request);

    Object customerDeactivateSms(Object request);


    // VIRTUAL CARD METHODS

    <E> Object createVirtualCard(HumoVirtualDto dto);

    <E> Object onSmsInfo(E value);

    Object getClientCards(HumoVirtualCardsDto dto);

    Object blockOrUnBlockCard(CardBlockDto dto);

    Object findCustomerP2PInfo(HumoCardDto humoCardDto);

    Object getBinList();

    Object getCardHistory(HumoHistoryRequestDTO requestDTO);

    Object tranSortedScoring(Object request);

    Object scoringByPersonCode(Object request);

    Object checkCard2PINFL(CheckCardToPinflRequestDTO request);

}

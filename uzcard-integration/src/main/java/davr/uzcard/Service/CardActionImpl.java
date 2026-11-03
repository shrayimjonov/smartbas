package davr.uzcard.Service;

import davr.uzcard.Entity.CardAction;
import davr.uzcard.Model.CardStatusDto;
import davr.uzcard.Model.Request.*;
import davr.uzcard.Repository.CardActionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CardActionImpl implements CardActionService{
    private final CardActionRepository actionRepository;

    @Override
    public <T> void createNewAction(T obj, String method, Long logProtocolId) {
       CardAction cardAction = new CardAction();
       cardAction.setCreatedTime(LocalDateTime.now());
       cardAction.setLogProtocolId(logProtocolId);
        switch (method){
            case "cards.new":
                CardsNew cardsNew = (CardsNew) obj;
                cardAction.setCard(cardsNew.getParams().getCard().getPan());
                cardAction.setParam1(cardsNew.getParams().getCard().getExpiry());
                cardAction.setMethod(method);
                actionRepository.save(cardAction);
                break;
            case "p2p.info":
                P2pInfo p2pInfo = (P2pInfo) obj;
                cardAction.setCard(p2pInfo.getParams().getHpan());
                cardAction.setMethod(method);
                actionRepository.save(cardAction);
                break;
            case "cards.get":
                CardsGet cardsGet = (CardsGet) obj;
                cardAction.setCard(cardsGet.getParams().getIds().get(0));
                cardAction.setMethod(method);
                actionRepository.save(cardAction);
                break;
            case "cards.get.list":
                CardsGet cardsGet2 = (CardsGet) obj;
                cardAction.setCard(cardsGet2.getParams().getIds().toString());
                cardAction.setMethod(method);
                actionRepository.save(cardAction);
                break;
            case "cards.list.token":
                CardsListToken cardsListToken = (CardsListToken) obj;
                cardAction.setMethod(method);
                cardAction.setAdditionalParam(cardsListToken.getParams().getPhone());
                actionRepository.save(cardAction);
                break;
            case "card.status.change":
                CardStatusChange statusChange = (CardStatusChange) obj;
                cardAction.setCard(statusChange.getParams().getCard().getCardId());
                cardAction.setMethod(method);
                cardAction.setParam1(statusChange.getParams().getCard().getExt());
                cardAction.setParam2("Change card status to: " + statusChange.getParams().getCard().getStatusId());
                cardAction.setParam3(statusChange.getParams().getCard().getStan());
                actionRepository.save(cardAction);
                break;
            case "card.pin.reset.otp":
                PinResetOtp pinResetOtp = (PinResetOtp) obj;
                cardAction.setCard(pinResetOtp.getParams().getCardId());
                cardAction.setMethod(method);
                actionRepository.save(cardAction);
                break;
            case "card.pin.reset.verify":
                PinResetVerify pinResetVerify = (PinResetVerify) obj;
                cardAction.setCard(pinResetVerify.getParams().getCard().getCardId());
                cardAction.setMethod(method);
                cardAction.setParam1(pinResetVerify.getParams().getCard().getExt());
                cardAction.setParam2("Code to verify: " + pinResetVerify.getParams().getCard().getCode());
                cardAction.setParam3(pinResetVerify.getParams().getCard().getStan());
                actionRepository.save(cardAction);
                break;
            case "balance.get":
                BalanceGet balanceGet = (BalanceGet) obj;
                cardAction.setCard(balanceGet.getParams().getIds().toString());
                cardAction.setMethod(method);
                actionRepository.save(cardAction);
                break;

        }

    }


}

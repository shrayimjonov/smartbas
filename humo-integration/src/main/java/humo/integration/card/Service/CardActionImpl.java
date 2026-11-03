package humo.integration.card.Service;

import humo.integration.card.Entity.CardAction;
import humo.integration.card.Model.Dto.HumoCardDto;
import humo.integration.card.Model.Dto.HumoPhoneDto;
import humo.integration.card.Model.Request.QueryTransHistoryReq;
import humo.integration.card.Repository.CardActionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
@RequiredArgsConstructor
public class CardActionImpl implements CardActionService {
    private final CardActionRepository actionRepository;

    @Override
    public <T> void createNewAction(T obj, String method, Long logProtocolId) {
        CardAction cardAction = new CardAction();
        cardAction.setCreatedTime(LocalDateTime.now(ZoneId.of("Asia/Tashkent")));
        cardAction.setLogProtocolId(logProtocolId);
        switch (method) {
            case "cardsByPhone":
                HumoPhoneDto humoPhoneDto = (HumoPhoneDto) obj;
                cardAction.setAdditionalParam(humoPhoneDto.getPhone());
                cardAction.setMethod(method);
                actionRepository.save(cardAction);
                break;
            case "cardsInfoV3":
                HumoCardDto dto = (HumoCardDto) obj;
                cardAction.setCard(dto.getCardNumber());
                cardAction.setMethod(method);
                actionRepository.save(cardAction);
                break;
            case "tranHistory":
                QueryTransHistoryReq transHistoryReq = (QueryTransHistoryReq) obj;
                cardAction.setCard(transHistoryReq.getPan());
                cardAction.setParam1(transHistoryReq.getBeginDate());
                cardAction.setParam2(transHistoryReq.getFinishDate());
                cardAction.setMethod(method);
                actionRepository.save(cardAction);
                break;
            case "cardNumber":
            case "maskedPan":
                String cardNumber = (String) obj;
                cardAction.setCard(cardNumber);
                cardAction.setMethod(method);
                actionRepository.save(cardAction);
                break;
            case "smsOn":
                cardAction.setAdditionalParam(obj.toString());
                actionRepository.save(cardAction);
                break;
        }

    }


}

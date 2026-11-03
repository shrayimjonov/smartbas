package humo.integration.card.Visa.controller;

import humo.integration.card.Model.Dto.core.response.CoreError;
import humo.integration.card.Visa.ListAccountsResp;
import humo.integration.card.Visa.Model.*;
import humo.integration.card.Visa.Service.VisaService;
import humo.integration.card.Visa.Service.VisaV2Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedList;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class VisaController {
    private final VisaService visaService;
    private final VisaV2Service visaV2Service;


    @PostMapping("/get_list")
    public ResponseEntity<?> visaInfo(@RequestBody CardRequest cardRequest) {
        var response = getCardResponse(cardRequest);

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    private CardResponse getCardResponse(CardRequest cardRequest) {
        try {
            List<CardResponse.Result> resultList = new LinkedList<>();

            for (String visaToken : cardRequest.getCard_ids()) {

                var request = RealCardRequest.builder().bankC("05").groupC("02").card(visaToken).build();
                CardInfoResp cardInfoResp = visaService.getCardInfo(request);

                if (cardInfoResp.getCARD() != null) {
                    GetCardBalanceResp balanceResp = visaService.getCardBalance(request);
                    ListAccountsResp accountsResp = visaService.getListAccounts(ListAccountsReq.builder()
                            .account(balanceResp.CARD_ACCT)
                            .build());


                    dataHistoryResp(resultList, visaToken, request, cardInfoResp, balanceResp, accountsResp);
                } else {
                    var cardResponse = CardResponse.Result.builder().holdAmount(0L).cashbackAmount(0L).id(visaToken).username("visa_middleware")
                            .pan("CARD NOT FOUND").build();
                    log.error("CARD NOT FOUND: {}", request.getCard());
                    resultList.add(cardResponse);
                }
            }


            return CardResponse.builder().jsonrpc("2.0").id(cardRequest.getRequest_id())
                    .result(resultList).build();
        } catch (Exception e) {
            log.error(e.getMessage());
            return CardResponse.builder().jsonrpc("2.0").id(cardRequest.getRequest_id())
                    .error(new CoreError(500, e.getMessage()))
                    .build();
        }
    }

    private void dataHistoryResp(List<CardResponse.Result> resultList,
                                 String visaToken, RealCardRequest request,
                                 CardInfoResp cardInfoResp,
                                 GetCardBalanceResp balanceResp,
                                 ListAccountsResp accountsResp) {
        // DataHistoryResp dataHistoryResp = visaService.getDataHistory(request);
        // DataHistoryResp dataHistoryResp = dataToCard.dataHistoryToCardResponse(visaService.getDataHistory2(request));
        var listCustomers = visaService.getListCustomers(request);
        PinCountResp pinCountResp = visaService.getPinCountTry(request);

        boolean sms = listCustomers.getR_MOB_PHONE() != null && listCustomers.getR_MOB_PHONE().startsWith("998");

        var cardResponse = CardResponse.Result.builder().holdAmount(0L).cashbackAmount(0L).id(visaToken).username("visa_middleware")
                .balance(Long.valueOf(balanceResp.getAVAIL_AMOUNT()))
                .pan(balanceResp.getCARD())
                .status(Integer.parseInt(balanceResp.getCARD_STATUS()))
                .expiry(balanceResp.getEXPIRY1().substring(2, 4) + balanceResp.getEXPIRY1().substring(5, 7))
                .fullName(cardInfoResp.getCARD_NAME())
                .cardtype("private")
                .phone(listCustomers.getR_MOB_PHONE())
                .sms(sms)
                .aacct("00981" + accountsResp.getTRANZ_ACCT())
                .pincnt(Integer.parseInt(pinCountResp.getPIN_TRY_COUNT()))
                .build();
        resultList.add(cardResponse);
    }
}

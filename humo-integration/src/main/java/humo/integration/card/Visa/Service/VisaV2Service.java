package humo.integration.card.Visa.Service;


import humo.integration.card.Exception.VisaException;
import humo.integration.card.Model.Dto.core.response.ResultData;
import humo.integration.card.Visa.ListAccountsResp;
import humo.integration.card.Visa.Model.*;
import humo.integration.card.Visa.Model.enums.CardType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class VisaV2Service {
    private final VisaFeign visaFeign;

    public CardResponseV2 cardInfo(InfoDTO dto) {
        log.info("Get Visa info: {}", dto.getCard());

        if (dto.getType() == CardType.TOKEN) {
            var panResult = getPan(dto.getCard());
            dto.setCard(panResult.getRCARD());
        }

        var accountResult = getAccountList(dto.getCard());

        if (!accountResult.getIsSuccess())
            throw new VisaException(accountResult.getError().getMessage());

        var transitResult = getListAccount(accountResult.getResult().getAccount().getBankAccountId());

        if (transitResult.getTRANZ_ACCT() == null)
            throw new VisaException(String.format("Transit account is null for : %s", dto.getCard()));

        var customerResult = getCustomer(dto.getCard());

        if (customerResult.getR_MOB_PHONE() == null)
            throw new VisaException(String.format("Mobile phone is null for : %s", dto.getCard()));


        var balanceResult = getBalance(dto);

        if (!balanceResult.getIsSuccess())
            throw new VisaException(balanceResult.getError().getMessage());

        return CardResponseV2.builder()
                .id(String.valueOf(System.currentTimeMillis()))
                .jsonrpc("2.0")
                .result(
                        CardResponseV2.Result.builder()
                                .id(dto.getCard())
                                .pan(dto.getCard())
                                .aacct("00981" + transitResult.getTRANZ_ACCT())
                                .par(accountResult.getResult().getAccount().getBankAccountId())
                                .phone(customerResult.getR_MOB_PHONE())
                                .sms(customerResult.getR_MOB_PHONE() != null && customerResult.getR_MOB_PHONE().startsWith("998"))
                                .cardtype("private")
                                .fullName(customerResult.getF_NAMES() + " " + customerResult.getSURNAME())
                                .balance(balanceResult.getResult().getAvailableAmount())
                                .cashbackAmount(0L)
                                .username("davrstip")
                                .expiry(dto.getExpiry())
                                .holdAmount(0L)
                                .build()

                )
                .build();
    }

    public CardOnlyBalanceResponse getCardBalance(InfoDTO dto) {
        log.info("Get Visa only balance: {}", dto.getCard());

        if (dto.getType() == CardType.TOKEN) {
            var panResult = getPan(dto.getCard());
            dto.setCard(panResult.getRCARD());
        }

        var infoResult = getInfo(dto.getCard());

        if (!infoResult.getIsSuccess())
            throw new VisaException(infoResult.getError().getMessage());

        var status = infoResult.getResult().getCard().getStatuses().getItem().stream()
                .filter(z -> z.getType().equals("card"))
                .findFirst()
                .orElseThrow(() -> new VisaException(String.format("Card: %s can't find card status", dto.getCard())));


        var balanceResult = getBalance(dto);

        if (!balanceResult.getIsSuccess())
            throw new VisaException(balanceResult.getError().getMessage());


        var pinResult = getPinCount(dto.getCard());

        if (pinResult.getPIN_TRY_COUNT() == null)
            throw new VisaException(String.format("Can't find pin count for card: %s", dto.getCard()));

        var customerResult = getCustomer(dto.getCard());

        return CardOnlyBalanceResponse.builder()
                .phone(customerResult.getR_MOB_PHONE())
                .status(status.getActionDescription().equals("Approved"))
                .holder(infoResult.getResult().getCard().getNameOnCard())
                .balance(balanceResult.getResult().getAvailableAmount())
                .pinCount(Integer.valueOf(pinResult.getPIN_TRY_COUNT()))
                .build();

    }

    private CardRealPan getPan(String token) {
        log.info("Get real pan from token: {}", token);
        try {
            var response = visaFeign.realCardRequest(RealPanRequest.builder()
                    .maskedPan(token)
                    .build());
            log.info("Get Real pan response:{} ", response);
            return response;
        } catch (Exception e) {
            throw new VisaException(e.getMessage());
        }
    }

    private AccountListV2Response getAccountList(String card) {
        log.info("Get account list for: {}", card);
        try {
            return visaFeign.getListAccountV2(CardV2Request.builder()
                    .card(card)
                    .build());
        } catch (Exception e) {
            throw new VisaException(e.getMessage());
        }
    }

    private ListAccountsResp getListAccount(String account) {
        log.info("Get account list for 22618%");
        try {
            return visaFeign.listAccounts(ListAccountsReq.builder()
                    .account(account)
                    .build());
        } catch (Exception e) {
            throw new VisaException(e.getMessage());
        }
    }

    private ListCustomersResp getCustomer(String card) {
        log.info("Get customer list for: {}", card);
        try {
            return visaFeign.listCustomers(RealCardRequest.builder()
                    .card(card)
                    .build());
        } catch (Exception e) {
            throw new VisaException(e.getMessage());
        }
    }

    private BalanceV2Response getBalance(InfoDTO dto) {
        log.info("Get Card: {} Balance from front", dto.getCard());
        try {
            return visaFeign.getBalanceV2(BalanceV2Request.builder()
                    .card(dto.getCard())
                    .expiry(dto.getExpiry())
                    .build());


        } catch (Exception e) {
            throw new VisaException(e.getMessage());
        }
    }

    private CardV2Response getInfo(String card) {
        log.info("Get Card getInfo: {} from front", card);
        try {
            return visaFeign.getCardInfoV2(CardV2Request.builder()
                    .card(card)
                    .build());
        } catch (Exception e) {
            throw new VisaException(e.getMessage());
        }
    }

    private PinCountResp getPinCount(String card) {
        log.info("Get Pin count for: {}", card);
        try {
            return visaFeign.pinCountResp(RealCardRequest.builder()
                    .card(card)
                    .build());
        } catch (Exception e) {
            throw new VisaException(e.getMessage());
        }
    }

    public ResultData<CardRealPan> takeRealPan(String token){
        log.info("Received command to take real pan for token: {}", token);

        CardRealPan response = getPan(token);

        ResultData<CardRealPan> resultData = new ResultData<>(response);

        log.info("Finished command to take real pan with response: {}", resultData);
        return resultData;
    }

}

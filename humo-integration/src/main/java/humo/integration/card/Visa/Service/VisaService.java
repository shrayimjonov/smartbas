package humo.integration.card.Visa.Service;

import humo.integration.card.Visa.ListAccountsResp;
import humo.integration.card.Visa.Model.*;
import humo.integration.card.Visa.Model.Convert.DataMob;
import lombok.var;
import org.springframework.stereotype.Service;

@Service
public class VisaService {
    private final VisaFeign visaFeign;

    public VisaService(VisaFeign visaFeign) {
        this.visaFeign = visaFeign;
    }



    public GetCardBalanceResp getCardBalance(RealCardRequest request) {
        return visaFeign.getCardBalanceResp(request);
    }

    public CardInfoResp getCardInfo(RealCardRequest request) {
        return visaFeign.cardInfoResp(request);
    }

    public DataHistoryResp getDataHistory(RealCardRequest request) {
        return visaFeign.dataHistoryResp(request);
    }

    public DataMob getDataHistory2(RealCardRequest request) {

        return visaFeign.dataHistoryResp2(request);
    }

    public PinCountResp getPinCountTry(RealCardRequest request) {
        return visaFeign.pinCountResp(request);
    }

    public ListCustomersResp getListCustomers(RealCardRequest request) {
        return visaFeign.listCustomers(request);
    }

    public ListAccountsResp getListAccounts(ListAccountsReq request) {
        return visaFeign.listAccounts(request);
    }
}

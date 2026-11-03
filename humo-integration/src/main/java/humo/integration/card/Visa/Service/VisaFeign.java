package humo.integration.card.Visa.Service;

import humo.integration.card.Visa.ListAccountsResp;
import humo.integration.card.Visa.Model.*;
import humo.integration.card.Visa.Model.Convert.DataMob;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

@FeignClient(url = "${visa.url}", name = "visaMiddle")
public interface VisaFeign {

    @PostMapping("/findRealCard")
    CardRealPan realCardRequest(@RequestBody RealPanRequest realCardRequest);

    @PostMapping("/findCardBalance")
    GetCardBalanceResp getCardBalanceResp(@RequestBody RealCardRequest realCardRequest);

    @PostMapping("/findCardInfo")
    CardInfoResp cardInfoResp(@RequestBody RealCardRequest realCardRequest);

    @PostMapping("/findPinTryCount")
    PinCountResp pinCountResp(@RequestBody RealCardRequest realCardRequest);

    @PostMapping("/findDataHistory")
    DataHistoryResp dataHistoryResp(@RequestBody RealCardRequest realCardRequest);

    @PostMapping(value = "/findDataHistory2")
    DataMob dataHistoryResp2(@RequestBody RealCardRequest realCardRequest);

    @PostMapping("/listCustomers")
    ListCustomersResp listCustomers(@RequestBody RealCardRequest realCardRequest);

    @PostMapping("/listAccounts")
    ListAccountsResp listAccounts(@RequestBody ListAccountsReq request);

    @PostMapping("/front/card")
    CardV2Response getCardInfoV2(@RequestBody @Valid CardV2Request request);

    @PostMapping("/front/balance")
    BalanceV2Response getBalanceV2(@RequestBody @Valid BalanceV2Request request);

    @PostMapping("/front/account")
    AccountListV2Response getListAccountV2(@RequestBody @Valid CardV2Request request);

}

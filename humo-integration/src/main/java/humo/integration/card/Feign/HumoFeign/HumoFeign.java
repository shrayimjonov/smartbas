package humo.integration.card.Feign.HumoFeign;

import humo.integration.card.Feign.FeignConfig.FeignConfig;
import humo.integration.card.Model.Request.CustomerListReq;
import humo.integration.card.Model.Request.HistoryMiddlewareRequest;
import humo.integration.card.Model.Request.IacsCardV3Req;
import humo.integration.card.Model.Response.CustomerListResp;
import humo.integration.card.Model.Response.IacsCardV3Resp;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "humo", url = "${humo.middleware}", configuration = {FeignConfig.class})
public interface HumoFeign {

    @PostMapping("/v2/mb/customer-list")
    CustomerListResp getCustomerCardsByPhone(@RequestBody CustomerListReq customerList);

    @PostMapping("/v3/iiacs/card")
    IacsCardV3Resp getCustomerCardInfo(@RequestBody IacsCardV3Req v3Req);

    @PostMapping("/v3/iiacs/card2")
    Object getCustomerP2PInfo(@RequestBody IacsCardV3Req v3Req);

    @PostMapping("/v2/mb/customer/activate")
    Object cardPhoneSmsOn(@RequestBody Object request);

    @PostMapping("/v2/mb/customer/deactivate")
    Object customerCardsSmsOf(@RequestBody Object request);

    @GetMapping("/v2/pm/bins")
    Object getBinList();

    @PostMapping("/v1/ws/transaction-history")
    Object getCardHistory(HistoryMiddlewareRequest request);

    @PostMapping("/cs/v1/transactions/scoring-sorted")
    Object tranSortedScoring(Object request);

    @PostMapping("/cs/v1/transactions/scoring-by-person-code")
    Object scoringByPersonCode(Object request);

    @PostMapping("/cs/v1/customer/cards/by-person-code/pan")
    Object checkCard2PINFL(Object request);

}

package humo.integration.card.Feign.HumoFeign;


import humo.integration.card.Model.Request.QueryTransHistoryReq;
import humo.integration.card.Model.Response.QueryTransHistResp;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "humoware", url = "${humo.humoware}")
public interface QueryTransFeign {

    @PostMapping("/humoHistory")
    QueryTransHistResp getCardHistoryByDate(@RequestBody QueryTransHistoryReq req);


}

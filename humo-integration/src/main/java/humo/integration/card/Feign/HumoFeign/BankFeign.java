package humo.integration.card.Feign.HumoFeign;


import humo.integration.card.Feign.FeignConfig.BankFeignConfig;
import humo.integration.card.Model.Response.HumoTransitAccountResp;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "bankService", url = "${bank.service}", configuration = BankFeignConfig.class)
public interface BankFeign {

    @GetMapping("/bank/acct?pan={dto}")
    HumoTransitAccountResp getHumoTransitAccount(@PathVariable String dto);


}

package nci.billing.feign;

import nci.billing.model.request.RepayMainDebtRequest;
import nci.billing.model.request.RepayMainDebtV2Request;
import nci.billing.model.response.RepayMainDebtResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "backAPI", url = "${nci.back-api.url}/back", configuration = BackAPIConfiguration.class)
public interface BackAPIFeign {
    @PostMapping("/v2/CloseLDSaldo")
    RepayMainDebtResponse[] repayMainDebt(@RequestBody RepayMainDebtRequest request);


    @PostMapping("/v2/BCloseLDSaldo")
    RepayMainDebtResponse[] repayMainDebtV2(@RequestBody RepayMainDebtV2Request request);


}

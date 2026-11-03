package uz.davrbank.getoutsideloanservice.aggregate.integration;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import uz.davrbank.getoutsideloanservice.aggregate.aop.GetOutsideLoanServiceClientFallback;
import uz.davrbank.getoutsideloanservice.aggregate.config.GlobalFeignConfiguration;
import uz.davrbank.getoutsideloanservice.aggregate.dto.integration.base.CollectionIntegrationResponseDTO;
import uz.davrbank.getoutsideloanservice.aggregate.dto.integration.base.PageableIntegrationResponseDTO;
import uz.davrbank.getoutsideloanservice.aggregate.dto.integration.request.GetOutsideLoanPageableRequest;
import uz.davrbank.getoutsideloanservice.aggregate.dto.integration.response.GetOutsideLoanPageableResponse;

@FeignClient(
        name = "getoutsideloanhub-service",
        contextId = "getoutsideloanhub-service",
        configuration = GlobalFeignConfiguration.class,
        path = "/hub/api",
        url = "http://127.0.0.1:7511",
        fallback = GetOutsideLoanServiceClientFallback.class
)
public interface GetOutsideLoanHubClient {

    @PostMapping("/outsite/loans/page")
    PageableIntegrationResponseDTO<GetOutsideLoanPageableResponse> getPage(
            @RequestBody GetOutsideLoanPageableRequest request
    );

    @PostMapping("/outsite/loans/for/excel")
    CollectionIntegrationResponseDTO<GetOutsideLoanPageableResponse> getForExcel(
            @RequestBody GetOutsideLoanPageableRequest request
    );
}

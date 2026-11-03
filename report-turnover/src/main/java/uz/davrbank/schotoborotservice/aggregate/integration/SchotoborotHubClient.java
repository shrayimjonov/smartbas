package uz.davrbank.schotoborotservice.aggregate.integration;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import uz.davrbank.schotoborotservice.aggregate.aop.SchotoborotServiceClientFallback;
import uz.davrbank.schotoborotservice.aggregate.config.FeignConfig;
import uz.davrbank.schotoborotservice.aggregate.dto.integration.base.PageableIntegrationResponseDTO;
import uz.davrbank.schotoborotservice.aggregate.dto.integration.request.SchotoborotGetPageableRequest;
import uz.davrbank.schotoborotservice.aggregate.dto.integration.response.SchotoborotGetPageableResponse;

@FeignClient(
        name = "schotoborothub-service",
        contextId = "schotoborotHubClient",
        configuration = FeignConfig.class,
        path = "/hub/api",
//        url = "http://127.0.0.1:7507",
        fallback = SchotoborotServiceClientFallback.class
)
public interface SchotoborotHubClient {

    @PostMapping("/schot/oborot/get/all/page")
    PageableIntegrationResponseDTO<SchotoborotGetPageableResponse> getAll(
            @RequestBody SchotoborotGetPageableRequest request
    );

    @PostMapping("/schot/oborot/page/for/excel")
    ResponseEntity<PageableIntegrationResponseDTO<SchotoborotGetPageableResponse>> getAllExcel(
            @RequestBody SchotoborotGetPageableRequest request
    );
}
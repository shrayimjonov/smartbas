package uz.davrbank.korinapogashenyaservice.aggregate.integration;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import uz.davrbank.korinapogashenyaservice.aggregate.aop.PogasheniyaServiceClientFallback;
import uz.davrbank.korinapogashenyaservice.aggregate.config.PogasheniyaHubFeignClientConfig;
import uz.davrbank.korinapogashenyaservice.aggregate.dto.integration.base.PageableIntegrationResponseDTO;
import uz.davrbank.korinapogashenyaservice.aggregate.dto.integration.request.PogasheniyaGetPageableRequest;
import uz.davrbank.korinapogashenyaservice.aggregate.dto.integration.response.PogasheniyaGetPageableResponse;

@FeignClient(
        name = "korinapogashenyahub-service",
        contextId = "korinapogashenyahub-service",
        configuration = PogasheniyaHubFeignClientConfig.class,
        path = "/hub/api",
//        url = "http://127.0.0.1:7508",
        fallback = PogasheniyaServiceClientFallback.class
)
public interface PogasheniyaHubClient {

    @PostMapping("/korina/pogasheniya/get/all/page")
    PageableIntegrationResponseDTO<PogasheniyaGetPageableResponse> getAll(
            @RequestBody PogasheniyaGetPageableRequest request
    );

    @PostMapping("/korina/pogasheniya/get/all/page")
    Object getAll1(
            @RequestBody PogasheniyaGetPageableRequest request
    );
}
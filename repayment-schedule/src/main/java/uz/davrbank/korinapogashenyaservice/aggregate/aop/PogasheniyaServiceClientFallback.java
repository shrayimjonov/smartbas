package uz.davrbank.korinapogashenyaservice.aggregate.aop;

import uz.davrbank.korinapogashenyaservice.aggregate.dto.integration.base.PageableIntegrationResponseDTO;
import uz.davrbank.korinapogashenyaservice.aggregate.dto.integration.request.PogasheniyaGetPageableRequest;
import uz.davrbank.korinapogashenyaservice.aggregate.dto.integration.response.PogasheniyaGetPageableResponse;
import uz.davrbank.korinapogashenyaservice.aggregate.integration.PogasheniyaHubClient;

public class PogasheniyaServiceClientFallback implements PogasheniyaHubClient {

    @Override
    public PageableIntegrationResponseDTO<PogasheniyaGetPageableResponse> getAll(PogasheniyaGetPageableRequest request) {
        throw new RuntimeException("Hub service unavailable. Fallback response returned.");
    }

    @Override
    public Object getAll1(PogasheniyaGetPageableRequest request) {
        return null;
    }
}

package uz.davrbank.schotoborotservice.aggregate.aop;

import org.springframework.http.ResponseEntity;
import uz.davrbank.schotoborotservice.aggregate.dto.integration.base.PageableIntegrationResponseDTO;
import uz.davrbank.schotoborotservice.aggregate.dto.integration.request.SchotoborotGetPageableRequest;
import uz.davrbank.schotoborotservice.aggregate.dto.integration.response.SchotoborotGetPageableResponse;
import uz.davrbank.schotoborotservice.aggregate.integration.SchotoborotHubClient;

public class SchotoborotServiceClientFallback implements SchotoborotHubClient {

    @Override
    public PageableIntegrationResponseDTO<SchotoborotGetPageableResponse> getAll(SchotoborotGetPageableRequest request) {
        throw new RuntimeException("Hub service unavailable. Fallback response returned.");
    }

    @Override
    public ResponseEntity<PageableIntegrationResponseDTO<SchotoborotGetPageableResponse>> getAllExcel(SchotoborotGetPageableRequest request) {
        throw new RuntimeException("Hub service unavailable. Fallback response returned.");
    }

/*    @Override
    public String getAllExcel(SchotoborotGetPageableRequest request) {
        throw new RuntimeException("Hub service unavailable. Fallback response returned.");
    }*/
}
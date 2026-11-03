package uz.davrbank.getoutsideloanservice.aggregate.aop;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import uz.davrbank.getoutsideloanservice.aggregate.dto.integration.base.CollectionIntegrationResponseDTO;
import uz.davrbank.getoutsideloanservice.aggregate.dto.integration.base.PageableIntegrationResponseDTO;
import uz.davrbank.getoutsideloanservice.aggregate.dto.integration.request.GetOutsideLoanPageableRequest;
import uz.davrbank.getoutsideloanservice.aggregate.dto.integration.response.GetOutsideLoanPageableResponse;
import uz.davrbank.getoutsideloanservice.aggregate.integration.GetOutsideLoanHubClient;

@Slf4j
@Component
public class GetOutsideLoanServiceClientFallback implements GetOutsideLoanHubClient {

    @Override
    public PageableIntegrationResponseDTO<GetOutsideLoanPageableResponse> getPage(GetOutsideLoanPageableRequest request) {
        log.error("Hub fallback triggered for page request: {}", request);
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Hub service unavailable.");
    }

    @Override
    public CollectionIntegrationResponseDTO<GetOutsideLoanPageableResponse> getForExcel(GetOutsideLoanPageableRequest request) {
        log.error("Hub fallback triggered for excel request: {}", request);
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Hub service unavailable.");
    }
}

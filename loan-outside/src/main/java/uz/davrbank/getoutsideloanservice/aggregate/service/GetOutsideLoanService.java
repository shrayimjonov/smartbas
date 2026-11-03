package uz.davrbank.getoutsideloanservice.aggregate.service;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import uz.davrbank.getoutsideloanservice.aggregate.dto.integration.base.PageableIntegrationResponseDTO;
import uz.davrbank.getoutsideloanservice.aggregate.dto.integration.response.GetOutsideLoanPageableResponse;
import uz.davrbank.getoutsideloanservice.aggregate.filter.GetOutsideLoanFilter;

public interface GetOutsideLoanService {

    ResponseEntity<PageableIntegrationResponseDTO<GetOutsideLoanPageableResponse>> getAllPageable(
            GetOutsideLoanFilter filter,
            String username
    );

    ResponseEntity<ByteArrayResource> downloadExcel(GetOutsideLoanFilter filter, String username);
}

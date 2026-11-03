package uz.davrbank.getoutsideloanservice.aggregate.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.davrbank.getoutsideloanservice.aggregate.dto.integration.base.PageableIntegrationResponseDTO;
import uz.davrbank.getoutsideloanservice.aggregate.dto.integration.response.GetOutsideLoanPageableResponse;
import uz.davrbank.getoutsideloanservice.aggregate.filter.GetOutsideLoanFilter;
import uz.davrbank.getoutsideloanservice.aggregate.service.GetOutsideLoanService;

@RestController
@RequestMapping("/v1/reports")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GetOutsideLoanController {

    GetOutsideLoanService getOutsideLoanService;

    @PostMapping("/get/all/page")
    public ResponseEntity<PageableIntegrationResponseDTO<GetOutsideLoanPageableResponse>> getAllPageable(
            @RequestBody GetOutsideLoanFilter filter,
            @RequestHeader(value = "username", required = false) String username
    ) {
        return getOutsideLoanService.getAllPageable(filter, username);
    }

    @PostMapping("/download/excel")
    public ResponseEntity<ByteArrayResource> downloadExcel(
            @RequestBody GetOutsideLoanFilter filter,
            @RequestHeader(value = "username", required = false) String username
    ) {
        return getOutsideLoanService.downloadExcel(filter, username);
    }
}

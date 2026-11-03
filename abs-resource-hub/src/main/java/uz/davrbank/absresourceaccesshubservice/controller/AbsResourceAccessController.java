package uz.davrbank.absresourceaccesshubservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.davrbank.absresourceaccesshubservice.aggregate.dto.absresourceaccess.request.GetAbsResourceAccessRequest;
import uz.davrbank.absresourceaccesshubservice.aggregate.dto.absresourceaccess.request.GetAbsUsersRequest;
import uz.davrbank.absresourceaccesshubservice.aggregate.dto.absresourceaccess.request.GetBranchRequest;
import uz.davrbank.absresourceaccesshubservice.aggregate.dto.absresourceaccess.response.GetAbsResourceAccessExcelResponse;
import uz.davrbank.absresourceaccesshubservice.aggregate.dto.absresourceaccess.response.GetAbsResourceAccessResponse;
import uz.davrbank.absresourceaccesshubservice.aggregate.dto.absresourceaccess.response.GetAbsUsersResponse;
import uz.davrbank.absresourceaccesshubservice.aggregate.dto.absresourceaccess.response.GetBranchResponse;
import uz.davrbank.absresourceaccesshubservice.service.AbsResourceAccessIntegrationService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/abs-resource-access")
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class AbsResourceAccessController {

    AbsResourceAccessIntegrationService integrationService;

    @PostMapping("/branches")
    public GetBranchResponse getBranchesPost(@Valid @RequestBody GetBranchRequest request) {
        log.info("getBranchesPost: {}", request);
        return integrationService.getBranches(request);
    }

    @PostMapping("/users")
    public GetAbsUsersResponse getAbsUsersPost(@Valid @RequestBody GetAbsUsersRequest request) {
        log.info("getAbsUsersPost: {}", request);
        return integrationService.getAbsUsers(request);
    }

    @PostMapping("/resources")
    public GetAbsResourceAccessResponse getAbsResourceAccessPost(
            @Valid @RequestBody GetAbsResourceAccessRequest request
    ) {
        log.info("getAbsResourceAccessPost: {}", request);
        return integrationService.getAbsResourceAccess(request);
    }

    @PostMapping("/resources/excel")
    public GetAbsResourceAccessExcelResponse getAbsResourceAccessExcelPost(
            @Valid @RequestBody GetAbsResourceAccessRequest request
    ) {
        log.info("getAbsResourceAccessExcelPost: {}", request);
        return integrationService.getAbsResourceAccessExcel(request);
    }
}

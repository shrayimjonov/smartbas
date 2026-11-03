package uz.davrbank.absresourceaccesshubservice.service;

import org.springframework.stereotype.Service;
import uz.davrbank.absresourceaccesshubservice.aggregate.dto.UniversalRequestDto;
import uz.davrbank.absresourceaccesshubservice.aggregate.dto.absresourceaccess.request.GetAbsResourceAccessRequest;
import uz.davrbank.absresourceaccesshubservice.aggregate.dto.absresourceaccess.request.GetAbsUsersRequest;
import uz.davrbank.absresourceaccesshubservice.aggregate.dto.absresourceaccess.request.GetBranchRequest;
import uz.davrbank.absresourceaccesshubservice.aggregate.dto.absresourceaccess.response.GetAbsResourceAccessExcelResponse;
import uz.davrbank.absresourceaccesshubservice.aggregate.dto.absresourceaccess.response.GetAbsResourceAccessResponse;
import uz.davrbank.absresourceaccesshubservice.aggregate.dto.absresourceaccess.response.GetAbsUsersResponse;
import uz.davrbank.absresourceaccesshubservice.aggregate.dto.absresourceaccess.response.GetBranchResponse;
import uz.davrbank.absresourceaccesshubservice.exception.condition.DatabaseException;
import uz.davrbank.absresourceaccesshubservice.exception.condition.ValidationException;

@Service
public class AbsResourceAccessIntegrationService {
    private static final String PACKAGE_NAME = "BAS_HUB_SPR";
    private static final String GET_BRANCH_PROCEDURE = PACKAGE_NAME + ".GetBranch";
    private static final String GET_ABS_USERS_PROCEDURE = PACKAGE_NAME + ".GetAbsUsers";
    private static final String GET_ABS_RESOURCE_ACCESS_PROCEDURE = PACKAGE_NAME + ".AbsResourceAccessPageable";
    private static final String GET_ABS_RESOURCE_ACCESS_EXCEL_PROCEDURE = PACKAGE_NAME + ".AbsResourceAccessExcel";

    private final CoreFundamentalUniversal coreFundamentalUniversal;

    public AbsResourceAccessIntegrationService(CoreFundamentalUniversal coreFundamentalUniversal) {
        this.coreFundamentalUniversal = coreFundamentalUniversal;
    }

    public GetBranchResponse getBranches(GetBranchRequest request) {
        validateBranchRequest(request);
        return execute(
                UniversalRequestDto.builder()
                        .procedureName(GET_BRANCH_PROCEDURE)
                        .callStatement("{ ? = call " + GET_BRANCH_PROCEDURE + "(?) }")
                        .inParams(new Object[]{request.getIBranchType()})
                        .build(),
                GetBranchResponse.class,
                GET_BRANCH_PROCEDURE
        );
    }

    public GetAbsUsersResponse getAbsUsers(GetAbsUsersRequest request) {
        validateUsersRequest(request);
        return execute(
                UniversalRequestDto.builder()
                        .procedureName(GET_ABS_USERS_PROCEDURE)
                        .callStatement("{ ? = call " + GET_ABS_USERS_PROCEDURE + "(?) }")
                        .inParams(new Object[]{request.getIBranch()})
                        .build(),
                GetAbsUsersResponse.class,
                GET_ABS_USERS_PROCEDURE
        );
    }

    public GetAbsResourceAccessResponse getAbsResourceAccess(GetAbsResourceAccessRequest request) {
        validateResourceAccessRequest(request);
        return execute(
                UniversalRequestDto.builder()
                        .procedureName(GET_ABS_RESOURCE_ACCESS_PROCEDURE)
                        .callStatement("{ ? = call " + GET_ABS_RESOURCE_ACCESS_PROCEDURE + "(?, ?, ?, ?) }")
                        .inParams(new Object[]{
                                request.getIBranch(),
                                request.getIUserId(),
                                request.getIPageNumber(),
                                request.getIPageSize()
                        })
                        .build(),
                GetAbsResourceAccessResponse.class,
                GET_ABS_RESOURCE_ACCESS_PROCEDURE
        );
    }

    public GetAbsResourceAccessExcelResponse getAbsResourceAccessExcel(GetAbsResourceAccessRequest request) {
        validateResourceAccessRequest(request);
        return execute(
                UniversalRequestDto.builder()
                        .procedureName(GET_ABS_RESOURCE_ACCESS_EXCEL_PROCEDURE)
                        .callStatement("{ ? = call " + GET_ABS_RESOURCE_ACCESS_EXCEL_PROCEDURE + "(?, ?, ?, ?) }")
                        .inParams(new Object[]{
                                request.getIBranch(),
                                request.getIUserId(),
                                request.getIPageNumber(),
                                request.getIPageSize()
                        })
                        .build(),
                GetAbsResourceAccessExcelResponse.class,
                GET_ABS_RESOURCE_ACCESS_EXCEL_PROCEDURE
        );
    }

    private <T> T execute(UniversalRequestDto request, Class<T> responseType, String procedureName) {
        try {
            return coreFundamentalUniversal.executeQueryUniversal(request, responseType);
        } catch (RuntimeException ex) {
            throw new DatabaseException(procedureName + " execution failed: " + getRootCauseMessage(ex));
        }
    }

    private void validateBranchRequest(GetBranchRequest request) {
        if (request == null || request.getIBranchType() == null) {
            throw new ValidationException("iBranchType", null, "iBranchType is required");
        }

        if (request.getIBranchType() < 0 || request.getIBranchType() > 1) {
            throw new ValidationException(
                    "iBranchType",
                    String.valueOf(request.getIBranchType()),
                    "iBranchType must be 0 or 1"
            );
        }
    }

    private void validateUsersRequest(GetAbsUsersRequest request) {
        if (request == null || isBlank(request.getIBranch())) {
            throw new ValidationException("iBranch", null, "iBranch is required");
        }
    }

    private void validateResourceAccessRequest(GetAbsResourceAccessRequest request) {
        if (request == null) {
            throw new ValidationException("request", null, "request is required");
        }

        if (isBlank(request.getIBranch())) {
            throw new ValidationException("iBranch", null, "iBranch is required");
        }

        if (request.getIUserId() == null || request.getIUserId() < 0) {
            throw new ValidationException(
                    "iUserId",
                    request.getIUserId() == null ? null : String.valueOf(request.getIUserId()),
                    "iUserId must be zero or greater"
            );
        }

        if (request.getIPageNumber() == null || request.getIPageNumber() < 1) {
            throw new ValidationException(
                    "iPageNumber",
                    request.getIPageNumber() == null ? null : String.valueOf(request.getIPageNumber()),
                    "iPageNumber must be greater than zero"
            );
        }

        if (request.getIPageSize() == null || request.getIPageSize() < 1) {
            throw new ValidationException(
                    "iPageSize",
                    request.getIPageSize() == null ? null : String.valueOf(request.getIPageSize()),
                    "iPageSize must be greater than zero"
            );
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    private String getRootCauseMessage(Throwable throwable) {
        Throwable current = throwable;
        while (current.getCause() != null) {
            current = current.getCause();
        }
        return current.getMessage() == null ? current.getClass().getSimpleName() : current.getMessage();
    }
}

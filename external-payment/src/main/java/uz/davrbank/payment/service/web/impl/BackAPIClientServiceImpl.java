package uz.davrbank.payment.service.web.impl;

import feign.RetryableException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uz.davrbank.payment.client.BackAPIFeignClient;
import uz.davrbank.payment.model.dto.backAPI.request.*;
import uz.davrbank.payment.model.dto.backAPI.response.BackCheckTranResponse;
import uz.davrbank.payment.model.dto.backAPI.response.BackForexResponse;
import uz.davrbank.payment.model.dto.backAPI.response.BackSimpleTranResponse;
import uz.davrbank.payment.model.dto.backAPI.response.BackVFXResponse;
import uz.davrbank.payment.service.web.BackAPIClientService;
import uz.davrbank.payment.utils.BranchUtils;
import uz.nodir.commondto.model.dto.core.response.SimpleResponse;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class BackAPIClientServiceImpl implements BackAPIClientService {
    private final BackAPIFeignClient client;

    @Override
    public SimpleResponse<BackForexResponse> forex(BackForexRequest request) {
        log.info("back API forex request: {}", request);

        var schemaResult = BranchUtils.getSchemaByBranch(request.getBranch());

        if(!schemaResult.getStatus())
            return new SimpleResponse<>(schemaResult.getError().getCode(), schemaResult.getError().getMessage());

        var schema = schemaResult.getData();

        try {
            var response = client.conversion(schema, request);
            log.info("back API forex response: {}", response);

            return new SimpleResponse<>(
                    response.getFirst()
            );
        } catch (RetryableException r) {
            log.error("back API get transaction by id method throws error: {}", r.getMessage());
            return new SimpleResponse<>(r.getMessage(), true);
        } catch (Exception e) {
            log.error("back API forex throws error: {}", e.getMessage());
            return new SimpleResponse<>(1114, e.getMessage());
        }
    }

    @Override
    public SimpleResponse<BackSimpleTranResponse> interBranch(BackInterBranchRequest request) {
        log.info("back API inter branch request: {}", request);

        var schemaResult = BranchUtils.getSchemaByBranch(request.getBranch());

        if(!schemaResult.getStatus())
            return new SimpleResponse<>(schemaResult.getError().getCode(), schemaResult.getError().getMessage());

        var schema = schemaResult.getData();
        try {
            var response = client.interBranch(schema, request);
            log.info("back API forex response: {}", response);

            return new SimpleResponse<>(
                    response.getFirst()
            );
        } catch (RetryableException r) {
            log.error("back API get transaction by id method throws error: {}", r.getMessage());
            return new SimpleResponse<>(r.getMessage(), true);
        } catch (Exception e) {
            log.error("back API interBranch throws error: {}", e.getMessage());
            return new SimpleResponse<>(1114, e.getMessage());
        }
    }

    @Override
    public SimpleResponse<BackSimpleTranResponse> simpleTransaction(BackSimpleTransactionRequest request) {
        log.info("back API simple transaction request: {}", request);
        var schemaResult = BranchUtils.getSchemaByBranch(request.getBranch());

        if(!schemaResult.getStatus())
            return new SimpleResponse<>(schemaResult.getError().getCode(), schemaResult.getError().getMessage());

        var schema = schemaResult.getData();

        try {
            var response = client.doSimpleTransaction(schema, request);
            log.info("back API simple transaction response: {}", response);

            return new SimpleResponse<>(
                    response.getFirst()
            );
        } catch (RetryableException r) {
            log.error("back API get transaction by id method throws error: {}", r.getMessage());
            return new SimpleResponse<>(r.getMessage(), true);
        } catch (Exception e) {
            log.error("back API simple transaction throws error: {}", e.getMessage());
            return new SimpleResponse<>(1114, e.getMessage());
        }
    }


    @Override
    public SimpleResponse<BackVFXResponse> vfxOperation(BackVFXRequest request) {
        log.info("back API vfx buy operation request: {}", request);

        var schemaResult = BranchUtils.getSchemaByBranch(request.getBranch());

        if(!schemaResult.getStatus())
            return new SimpleResponse<>(schemaResult.getError().getCode(), schemaResult.getError().getMessage());

        var schema = schemaResult.getData();

        try {
            var response = client.vfxOperation(schema, request);
            log.info("back API vfx buy operation response: {}", response);

            return new SimpleResponse<>(
                    response.getFirst()
            );
        } catch (RetryableException r) {
            log.error("back API get transaction by id method throws error: {}", r.getMessage());
            return new SimpleResponse<>(r.getMessage(), true);
        } catch (Exception e) {
            log.error("back API vfx buy operation throws error: {}", e.getMessage());
            return new SimpleResponse<>(1115, e.getMessage());
        }
    }


    @Override
    public SimpleResponse<List<BackCheckTranResponse>> getTranInfoByExtId(String branch, String extId) {
        log.info("back API get transaction by id request: {}", extId);
        var schemaResult = BranchUtils.getSchemaByBranch(branch);

        if(!schemaResult.getStatus())
            return new SimpleResponse<>(schemaResult.getError().getCode(), schemaResult.getError().getMessage());

        var schema = schemaResult.getData();

        var request = new BackCheckTranRequest(extId);
        try {
            var response = client.getTransactionById(schema, request);
            log.info("back API get transaction by id response: {}", response);

            return new SimpleResponse<>(response);
        } catch (RetryableException r) {
            log.error("back API get transaction by id method throws error: {}", r.getMessage());
            return new SimpleResponse<>(r.getMessage(), true);
        } catch (Exception e) {
            log.error("back API get transaction by id method throws error: {}", e.getMessage());
            return new SimpleResponse<>(1115, e.getMessage());
        }
    }


}

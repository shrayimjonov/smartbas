package uz.davrbank.payment.service.web.impl;

import feign.FeignException;
import feign.RetryableException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uz.davrbank.payment.client.ABSHubFeignClient;
import uz.davrbank.payment.model.dto.absHub.request.AbsHubTransactionReverseRequest;
import uz.davrbank.payment.model.dto.absHub.response.AbsHubTransactionReverseResponse;
import uz.davrbank.payment.service.web.AbsHubClientService;
import uz.nodir.commondto.model.dto.core.response.ResultData;
import uz.nodir.commondto.model.dto.core.response.SimpleResponse;

@Service
@Slf4j
@RequiredArgsConstructor
public class AbsHubClientServiceImpl implements AbsHubClientService {
    private final ABSHubFeignClient client;

    @Override
    public SimpleResponse<ResultData<AbsHubTransactionReverseResponse>> reverse(AbsHubTransactionReverseRequest request) {
        log.info("Reverse transaction request to abs hub: {}", request);
        try {
            var response = client.reverse(request);
            log.info("Reverse transaction from abs hub response: {}", response);
            return new SimpleResponse<>(response);
        } catch (RetryableException r) {
            log.info("Reverse transaction throws time out error: {}", r.getMessage());
            return new SimpleResponse<>(580, r.getMessage());
        } catch (FeignException f) {
            log.info("Reverse transaction throws feign error: {}", f.getMessage());
            return new SimpleResponse<>(581, f.getMessage());
        } catch (Exception e) {
            log.info("Reverse transaction throws error: {}", e.getMessage());
            return new SimpleResponse<>(582, e.getMessage());
        }
    }
}

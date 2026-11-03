package uz.davrbank.payment.service.web.impl;

import feign.FeignException;
import feign.RetryableException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uz.davrbank.payment.client.BillingFeignClient;
import uz.davrbank.payment.model.dto.billing.request.CancelRequest;
import uz.davrbank.payment.model.dto.billing.request.CheckRequest;
import uz.davrbank.payment.model.dto.billing.request.HoldRequest;
import uz.davrbank.payment.model.dto.billing.request.PayRequest;
import uz.davrbank.payment.model.dto.billing.response.BillingBaseResponse;
import uz.davrbank.payment.model.dto.billing.response.BillingCancelHoldResponse;
import uz.davrbank.payment.model.dto.billing.response.result.DataResult;
import uz.davrbank.payment.model.dto.billing.response.result.SRData;
import uz.davrbank.payment.service.web.BillingClientService;
import uz.nodir.commondto.exception.GatewayException;
import uz.nodir.commondto.exception.GeneralErrorException;
import uz.nodir.commondto.exception.TimeOutException;

@Service
@RequiredArgsConstructor
@Slf4j
public class BillingServiceImpl implements BillingClientService {
    private final BillingFeignClient client;

    @Override
    public BillingBaseResponse<Object> check(CheckRequest request) {
        log.info("Billing sending check request: {}", request);
        try {
            var response = client.check(request);
            log.info("Billing check response: {}", response);
            return response;
        } catch (RetryableException r) {
            throw new TimeOutException(r.getMessage());
        } catch (FeignException f) {
            throw new GatewayException(f.getMessage());
        } catch (Exception e) {
            throw new GeneralErrorException(e.getMessage());
        }
    }

    @Override
    public BillingBaseResponse<DataResult<SRData>> pay(PayRequest request) {
        log.info("Billing sending pay request: {}", request);
        try {
            var response = client.pay(request);
            log.info("Billing pay response: {}", response);
            return response;
        } catch (RetryableException r) {
            throw new TimeOutException(r.getMessage());
        } catch (FeignException f) {
            throw new GatewayException(f.getMessage());
        } catch (Exception e) {
            throw new GeneralErrorException(e.getMessage());
        }
    }


    @Override
    public BillingBaseResponse<Object> cancel(CancelRequest request) {
        log.info("Billing sending cancel request: {}", request);
        try {
            var response = client.cancel(request);
            log.info("Billing cancel response: {}", response);
            return response;
        } catch (RetryableException r) {
            throw new TimeOutException(r.getMessage());
        } catch (FeignException f) {
            throw new GatewayException(f.getMessage());
        } catch (Exception e) {
            throw new GeneralErrorException(e.getMessage());
        }
    }

    @Override
    public BillingBaseResponse<Object> holdActive(HoldRequest request) {
        log.info("Billing sending hold active request: {}", request);
        try {
            var response = client.holdActive(request);
            log.info("Billing hold active response: {}", response);
            return response;
        } catch (RetryableException r) {
            throw new TimeOutException(r.getMessage());
        } catch (FeignException f) {
            throw new GatewayException(f.getMessage());
        } catch (Exception e) {
            throw new GeneralErrorException(e.getMessage());
        }
    }


    @Override
    public BillingBaseResponse<DataResult<SRData>> holdConfirm(HoldRequest request) {
        log.info("Billing sending hold confirm request: {}", request);
        try {
            var response = client.holdConfirm(request);
            log.info("Billing hold confirm response: {}", response);
            return response;
        } catch (RetryableException r) {
            log.info("Taken a time out error: {}", r.getMessage());
            throw new TimeOutException(r.getMessage());
        } catch (FeignException f) {
            log.info("Taken a third party error: {}", f.getMessage());
            throw new GatewayException(f.getMessage());
        } catch (Exception e) {
            log.info("Taken an internal error: {}", e.getMessage());
            throw new GeneralErrorException(e.getMessage());
        }
    }

    @Override
    public BillingCancelHoldResponse holdCancel(Long id) {
        log.info("Billing sending hold cancel request: {}", id);
        try {
            var response = client.holdCancel(id);
            log.info("Billing hold cancel response: {}", response);
            return response;
        } catch (Exception e) {
            log.error("Billing hold cancel throws error: {}", e.getMessage());
            return new BillingCancelHoldResponse(false);
        }
    }
}

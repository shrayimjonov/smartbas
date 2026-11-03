package humo.integration.card.Service.impl;


import humo.integration.card.Exception.ThirdPartyException;
import humo.integration.card.Feign.HumoFeign.HumoCardLimitFeignClient;
import humo.integration.card.Service.LimitCardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import lombok.var;


@Service
@RequiredArgsConstructor
@Slf4j
public class LimitCardServiceImpl implements LimitCardService {

    private final HumoCardLimitFeignClient client;

    @Override
    public Object getLimitBalance(Object request) {
        log.debug("Sending get limit balance request to Humo Card Limit service with request: {}", request);
        try {
            var response = client.getLimitBalance(request);
            log.debug("Received successful response for get limit balance from Humo Card Limit service with response: {}", response);
            return response;
        } catch (Exception e) {
            log.error("Error while calling getLimitBalance from Humo Card Limit service. Error: {}", e.getMessage(), e);
            throw new ThirdPartyException(e.getMessage());
        }
    }

    @Override
    public Object getRefData() {
        try {
            var response = client.getRefData();
            log.debug("Received successful response for get reference data from Humo Card Limit service with response: {}", response);
            return response;
        } catch (Exception e) {
            log.error("Error while calling getRefData from Humo Card Limit service. Error: {}", e.getMessage(), e);
            throw new ThirdPartyException(e.getMessage());
        }
    }

    @Override
    public Object setAnyLimit(Object request) {
        log.debug("Sending set any limit request to Humo Card Limit service with request: {}", request);
        try {
            var response = client.setAnyLimit(request);
            log.debug("Received successful response for set any limit from Humo Card Limit service with response: {}", response);
            return response;
        } catch (Exception e) {
            log.error("Error while calling setAnyLimit from Humo Card Limit service. Error: {}", e.getMessage(), e);
            throw new ThirdPartyException(e.getMessage());
        }
    }

    @Override
    public Object editAnyLimit(Object request) {
        log.debug("Sending edit any limit request to Humo Card Limit service with request: {}", request);
        try {
            var response = client.editAnyLimit(request);
            log.debug("Received successful response for edit any limit from Humo Card Limit service with response: {}", response);
            return response;
        } catch (Exception e) {
            log.error("Error while calling editAnyLimit from Humo Card Limit service. Error: {}", e.getMessage(), e);
            throw new ThirdPartyException(e.getMessage());
        }
    }

    @Override
    public Object getSubjectValue(Object request) {
        log.debug("Sending get subject value request to Humo Card Limit service with request: {}", request);
        try {
            var response = client.getSubjectValue(request);
            log.debug("Received successful response for get subject value from Humo Card Limit service with response: {}", response);
            return response;
        } catch (Exception e) {
            log.error("Error while calling getSubjectValue from Humo Card Limit service. Error: {}", e.getMessage(), e);
            throw new ThirdPartyException(e.getMessage());
        }
    }

    @Override
    public Object deleteLimit(Object request) {
        log.debug("Sending delete limit request to Humo Card Limit service with request: {}", request);
        try {
            var response = client.deleteLimit(request);
            log.debug("Received successful response for delete limit from Humo Card Limit service with response: {}", response);
            return response;
        } catch (Exception e) {
            log.error("Error while calling deleteLimit from Humo Card Limit service. Error: {}", e.getMessage(), e);
            throw new ThirdPartyException(e.getMessage());
        }
    }

    @Override
    public Object deleteAllLimit(Object request) {
        log.debug("Sending delete all limits request to Humo Card Limit service with request: {}", request);
        try {
            var response = client.deleteAllLimit(request);
            log.debug("Received successful response for delete all limits from Humo Card Limit service with response: {}", response);
            return response;
        } catch (Exception e) {
            log.error("Error while calling deleteAllLimit from Humo Card Limit service. Error: {}", e.getMessage(), e);
            throw new ThirdPartyException(e.getMessage());
        }
    }
}
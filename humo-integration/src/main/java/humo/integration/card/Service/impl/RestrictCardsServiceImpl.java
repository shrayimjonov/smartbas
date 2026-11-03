package humo.integration.card.Service.impl;

import humo.integration.card.Exception.ThirdPartyException;
import humo.integration.card.Feign.HumoFeign.HumoCardRestrictFeignClient;
import humo.integration.card.Service.RestrictCardsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.stereotype.Service;


@Service
@Slf4j
@RequiredArgsConstructor
public class RestrictCardsServiceImpl implements RestrictCardsService {
    private final HumoCardRestrictFeignClient client;

    @Override
    public Object getBlockedCards() {
        log.debug("Sending get blocked cards method request to restrict cards service");
        try {
            var response = client.getBlockedCards();
            log.debug("Received result from restrict cards method by get blocked cards method with response: {}", response);
            return response;
        } catch (Exception e) {
            throw new ThirdPartyException(e.getMessage());
        }
    }

    @Override
    public Object getCard(Object request) {
        log.debug("Sending get one card method request to restrict cards service with data: {}", request);
        try {
            var response = client.getCard(request);
            log.debug("Received result from restrict cards method by get one card method with response: {}", response);
            return response;
        } catch (Exception e) {
            throw new ThirdPartyException(e.getMessage());
        }
    }

    @Override
    public Object addCard(Object request) {
        log.debug("Sending block card method request to restrict cards service with data: {}", request);
        try {
            var response = client.addCard(request);
            log.debug("Received result from restrict cards method by block card method with response: {}", response);
            return response;
        } catch (Exception e) {
            throw new ThirdPartyException(e.getMessage());
        }
    }

    @Override
    public Object removeCard(Object request) {
        log.debug("Sending unBlock card method request to restrict cards service with data: {}", request);
        try {
            var response = client.removeCard(request);
            log.debug("Received result from restrict cards method by unBlock card method with response: {}", response);
            return response;
        } catch (Exception e) {
            throw new ThirdPartyException(e.getMessage());
        }
    }
}

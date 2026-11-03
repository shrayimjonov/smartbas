package uz.davrbank.payment.service.web.impl;

import feign.FeignException;
import feign.RetryableException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uz.davrbank.payment.client.CardFeignClient;
import uz.davrbank.payment.model.dto.core.response.CardWithStatusDTO;
import uz.davrbank.payment.service.web.CardClientService;
import uz.nodir.commondto.exception.GatewayException;
import uz.nodir.commondto.exception.GeneralErrorException;
import uz.nodir.commondto.exception.TimeOutException;
import uz.nodir.commondto.model.dto.core.response.ResultData;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CardServiceImpl implements CardClientService {
    private final CardFeignClient client;

    @Override
    public ResultData<CardWithStatusDTO> getCardById(UUID cardId) {
        log.info("Get card by id: {}", cardId);
        try {
            var response = client.getCardById(cardId);
            log.info("Card get by id response: {}", response);
            return response;
        } catch (RetryableException r) {
            throw new TimeOutException(r.getMessage());
        } catch (FeignException f) {
            throw new GatewayException(f.getMessage());
        } catch (Exception e) {
            throw new GeneralErrorException(e.getMessage());
        }
    }

}

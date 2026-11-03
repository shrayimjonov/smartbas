package uz.davrbank.payment.service.web;

import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import uz.davrbank.payment.model.dto.core.response.CardWithStatusDTO;
import uz.nodir.commondto.exception.GatewayException;
import uz.nodir.commondto.exception.GeneralErrorException;
import uz.nodir.commondto.exception.TimeOutException;
import uz.nodir.commondto.model.dto.core.response.ResultData;

import java.util.UUID;

public interface CardClientService {
    @Retryable(retryFor = {
            TimeOutException.class,
            GatewayException.class,
            GeneralErrorException.class
    }, maxAttempts = 4, backoff = @Backoff(delay = 1500))
    ResultData<CardWithStatusDTO> getCardById(UUID cardId);
}

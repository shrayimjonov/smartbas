package uz.davrbank.payment.client;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import uz.davrbank.payment.client.configuration.CardFeignConfiguration;
import uz.davrbank.payment.model.dto.core.response.CardWithStatusDTO;
import uz.nodir.commondto.model.dto.core.response.ResultData;

import java.util.UUID;

@FeignClient(value = "card-external", configuration = CardFeignConfiguration.class)
public interface CardFeignClient {
    @GetMapping("/card/api/v1/internal/cardById/{cardId}")
    ResultData<CardWithStatusDTO> getCardById(@PathVariable("cardId") UUID cardId);

}

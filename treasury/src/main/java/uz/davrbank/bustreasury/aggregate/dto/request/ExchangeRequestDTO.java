package uz.davrbank.bustreasury.aggregate.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ExchangeRequestDTO {
    private Long total;
    private Long exchangeRate;
    private Long templateId;
    private String worker;
}
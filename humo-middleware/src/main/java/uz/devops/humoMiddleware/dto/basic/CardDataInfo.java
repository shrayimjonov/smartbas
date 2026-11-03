package uz.devops.humoMiddleware.dto.basic;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CardDataInfo {
    private CardInfo card;
    @JsonProperty(value = "mb_agreement")
    private AgreementInfo agreement;
}

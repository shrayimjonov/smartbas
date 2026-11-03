package uz.davrbank.accounttocardservice.aggregate.dto.request.fundamental;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UzCardRequestDataDto implements Serializable {

    @JsonProperty("amount")
    private Long amount;
    @JsonProperty("cardId")
    private String cardId;
    @JsonProperty("ext")
    private String ext;
    @JsonProperty("merchantId")
    private String merchantId;
    @JsonProperty("terminalId")
    private String terminalId;
    @JsonProperty("port")
    private Integer port;
    @JsonProperty("stan")
    private String stan;
}

// 4023 0602 2291 2080
//Alijon yolchiyev
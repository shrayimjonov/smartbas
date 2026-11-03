package soap.humo_gateway.dto.response.core;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class FaultDto {
    private String faultcode;
    private Object faultstring;
    private Detail detail;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Detail {
        @JsonProperty("ebppif1:PaymentServerException")
        private PaymentServerException paymentServerException;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PaymentServerException {
        private String provider;
        private int error;
        private String description;
        private String screen;
    }

}

package soap.humo_gateway.dto.response.humoIntegration;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class BinResponse {
    private String id;
    private BinResult result;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class BinResult {
        private ArrayList<Datum> data;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Datum {
        private String bank_c;
        private String bank_name;
        private String bin_code;
        private String bin_owner;
        private String isVirtual;
        private String isCobadge;
        private String limited_in_epos;
    }

}

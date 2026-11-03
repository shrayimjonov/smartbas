package davr.yeisvo.dto.response.spravochniki;

import com.fasterxml.jackson.annotation.JsonProperty;
import davr.yeisvo.dto.response.BaseResponse;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DictionaryBankResponse extends BaseResponse {
    private  Long gtkId;
    private List<DictionaryBank> banks;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DictionaryBank {

        private Long id;  //bu bizdan qaytarishda beramiz
        @JsonProperty("bankInn")
        private String bankInn;

        @JsonProperty("bankId")
        private String bankId;

        @JsonProperty("bankClass")
        private String bankClass;

        @JsonProperty("regionId")
        private String regionId;

        @JsonProperty("bankType")
        private Integer bankType;

        @JsonProperty("bankName")
        private String bankName;

        @JsonProperty("bankAddress")
        private String bankAddress;

        @JsonProperty("active")
        private String active;

        @JsonProperty("newBankInn")
        private String newBankInn;

        @JsonProperty("newBankId")
        private String newBankId;

        @JsonProperty("parentNewBankInn")
        private String parentNewBankInn;

        @JsonProperty("parentNewBankId")
        private String parentNewBankId;
    }
}

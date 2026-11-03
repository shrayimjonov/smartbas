package uz.davrbank.korinapogashenyaservice.aggregate.dto.integration.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PogasheniyaGetPageableResponse {

    @JsonProperty("LD_ID")
    Long ldId;

    @JsonProperty("STATE")
    String state;

    @JsonProperty("ACCOUNT")
    String account;

    @JsonProperty("KOD_VAL")
    String codVal;

    @JsonProperty("BRANCH")
    String branch;

    @JsonProperty("CLIENT_NAME")
    String clientName;

    @JsonProperty("SUMMA_1_7")
    Long summa17;

    @JsonProperty("SUMMA_8_30")
    Long summa830;

    @JsonProperty("SUMMA_31_90")
    Long summa3190;

    @JsonProperty("SUMMA_91_180")
    Long summa91180;

    @JsonProperty("SUMMA_181_365")
    Long summa181365;

    @JsonProperty("SUMMA_366_730")
    Long summa366730;

    @JsonProperty("SUMMA_731_1095")
    Long summa7311095;

    @JsonProperty("SUMMA_1096_1825")
    Long summa10961825;

    @JsonProperty("SUMMA_1826_3650")
    Long summa18263650;

    @JsonProperty("SUMMA_1_3651")
    Long summa13651;
}

package uz.davrbank.korinapogashenyaservice.aggregate.dto.integration.base;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BaseIntegrationResponseDTO {

    @JsonProperty("status")
    String status;
    @JsonProperty("error")
    String error;
}

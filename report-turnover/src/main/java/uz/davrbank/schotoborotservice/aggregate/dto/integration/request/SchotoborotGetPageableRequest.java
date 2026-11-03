package uz.davrbank.schotoborotservice.aggregate.dto.integration.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SchotoborotGetPageableRequest {

    @JsonProperty("iStartDate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
    String iStartDate;

    @JsonProperty("iEndDate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
    String iEndDate;

    @JsonProperty("iAccount")
    String iAccount;

    @JsonProperty("iPageNumber")
    Integer iPageNumber;

    @JsonProperty("iPageSize")
    Integer iPageSize;
}
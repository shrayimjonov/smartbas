package uz.davrbank.getoutsideloanservice.aggregate.dto.integration.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GetOutsideLoanPageableRequest {

    @JsonProperty("startDate")
    String startDate;

    @JsonProperty("endDate")
    String endDate;

    @JsonProperty("branch")
    String branch;

    @JsonProperty("state")
    Integer state;

    @JsonProperty("pageNumber")
    Integer pageNumber;

    @JsonProperty("pageSize")
    Integer pageSize;
}

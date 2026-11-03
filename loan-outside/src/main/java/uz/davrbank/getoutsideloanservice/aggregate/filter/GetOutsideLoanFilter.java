package uz.davrbank.getoutsideloanservice.aggregate.filter;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GetOutsideLoanFilter extends BaseFilter {

    @JsonProperty("startDate")
    String startDate;

    @JsonProperty("endDate")
    String endDate;

    @JsonProperty("branch")
    String branch;

    @JsonProperty("state")
    Integer state;
}

package uz.davrbank.korinapogashenyaservice.aggregate.filter;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BaseFilter {

    @JsonProperty("pageNumber")
    Integer pageNumber = 1;

    @JsonProperty("pageSize")
    Integer pageSize = 20;
}

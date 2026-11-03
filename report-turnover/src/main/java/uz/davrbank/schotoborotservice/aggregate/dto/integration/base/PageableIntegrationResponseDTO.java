package uz.davrbank.schotoborotservice.aggregate.dto.integration.base;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PageableIntegrationResponseDTO<T> extends BaseIntegrationResponseDTO {

    @JsonProperty("totalElements")
    Integer totalElements;

    @JsonProperty("totalPages")
    Integer totalPages;

    @JsonProperty("last")
    Boolean last;

    @JsonProperty("pageNumber")
    Integer pageNumber;

    @JsonProperty("pageSize")
    Integer pageSize;

    @JsonProperty("numberOfElements")
    Integer numberOfElements;

    @JsonProperty("first")
    Boolean first;

    @JsonProperty("empty")
    Boolean empty;

    @JsonProperty("data")
    List<T> data;
}

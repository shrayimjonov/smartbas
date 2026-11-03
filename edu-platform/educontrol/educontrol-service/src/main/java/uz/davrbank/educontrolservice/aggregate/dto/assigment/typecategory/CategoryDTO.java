package uz.davrbank.educontrolservice.aggregate.dto.assigment.typecategory;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CategoryDTO(
        String name,
        String description,
        @JsonProperty("type_id")
        String typeId
        ) {
}

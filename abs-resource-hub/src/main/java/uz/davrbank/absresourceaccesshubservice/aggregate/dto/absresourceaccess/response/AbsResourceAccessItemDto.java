package uz.davrbank.absresourceaccesshubservice.aggregate.dto.absresourceaccess.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AbsResourceAccessItemDto {
    @JsonProperty("LOGIN")
    private String login;

    @JsonProperty("MODUL")
    private String modul;

    @JsonProperty("ACTION")
    private String action;
}

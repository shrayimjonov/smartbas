package uz.davrbank.filescloudservice.aggrecation.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CoreError(Integer code, String message) {
}

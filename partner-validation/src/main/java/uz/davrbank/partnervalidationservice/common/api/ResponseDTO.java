package uz.davrbank.partnervalidationservice.common.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import uz.davrbank.partnervalidationservice.exception.ErrorResponse;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ResponseDTO<T> {

    @JsonProperty("success")
    boolean success;

    @JsonProperty("data")
    T data;

    @JsonProperty("error")
    ErrorResponse error;

    private ResponseDTO(boolean success, T data, ErrorResponse error) {
        this.success = success;
        this.data = data;
        this.error = error;
    }

    private ResponseDTO(T data) {
        this(true, data, null);
    }

    private ResponseDTO(ErrorResponse error, boolean success) {
        this(success, null, error);
    }

    public static <T> ResponseDTO<T> ok(T body) {
        return new ResponseDTO<>(body);
    }

    public static ResponseDTO<ErrorResponse> error(ErrorResponse error) {
        return new ResponseDTO<>(error, false);
    }
}

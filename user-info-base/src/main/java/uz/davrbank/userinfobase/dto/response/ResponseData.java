package uz.davrbank.userinfobase.dto.response;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import uz.davrbank.userinfobase.enums.ResponseStatus;

import java.io.Serializable;

@Getter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseData implements Serializable {
    public static final ResponseData SUCCESS = new ResponseData(true);
    public static final ResponseData FAILED = new ResponseData(false);
    private boolean success = false;
    private Integer code = 400;
    private Error error;


    public ResponseData(boolean success) {
        this.success = success;
        this.code = 200;
    }


    public ResponseData(boolean success, ResponseStatus status, String exception) {
        this.success = success;
        this.error = new Error(status.getCode(), exception);
    }


    @Getter
    @Setter
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Error {
        private Integer code;
        private String error;

    }

}

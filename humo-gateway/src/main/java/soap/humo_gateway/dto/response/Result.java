package soap.humo_gateway.dto.response;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.io.Serializable;

@Getter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Result implements Serializable {
    private String status = "FAILED";
    private boolean success = false;
    private Error error;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Error {
        private Long code;
        private String message;
    }

    public Result(boolean success) {
        this.status = "SUCCESS";
        this.success = success;
    }

    public Result(boolean success, Long code, String message) {
        this.success = success;
        this.error = new Error(code, message);
    }

}

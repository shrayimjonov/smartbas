package humo.integration.card.Model.Dto.core.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import humo.integration.card.Model.enums.ResponseStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
public class ResultData<R> {
    private Integer successCode;
    private Boolean isSuccess;
    private ResponseStatus status;
    private R result;
    private CoreError error;
    private Long timeStamp = System.currentTimeMillis();

    public ResultData(R result) {
        this.successCode = 0;
        this.isSuccess = true;
        this.status = ResponseStatus.SUCCESS;
        this.result = result;
    }

    public ResultData(Integer code, Exception e) {
        this.successCode = -1;
        this.isSuccess = false;
        this.status = ResponseStatus.FAILED;
        this.error = new CoreError(code, e.getMessage());
    }

    public ResultData(Integer code, String message) {
        this.successCode = -1;
        this.isSuccess = false;
        this.status = ResponseStatus.FAILED;
        this.error = new CoreError(code, message);
    }


    public static <T> ResultData<T> ok(@NotNull T body) {
        return new ResultData<>(body);
    }


}

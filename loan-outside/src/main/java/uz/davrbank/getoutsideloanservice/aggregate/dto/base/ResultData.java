package uz.davrbank.getoutsideloanservice.aggregate.dto.base;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import uz.davrbank.getoutsideloanservice.aggregate.enums.ResponseStatus;

@Getter
@Setter
@ToString
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResultData<R> {

    Integer successCode;
    Boolean isSuccess;
    ResponseStatus status;
    R result;
    CodeError error;
    Long timeStamp = System.currentTimeMillis();
    Integer count;

    public ResultData(R result) {
        this.successCode = 0;
        this.isSuccess = true;
        this.status = ResponseStatus.SUCCESS;
        this.result = result;
    }

    public ResultData(CodeError codeError, boolean isSuccess) {
        this.successCode = -1;
        this.isSuccess = isSuccess;
        this.status = ResponseStatus.FAILED;
        this.error = codeError;
    }

    public static <T> ResultData<T> ok(@NotNull T body) {
        return new ResultData<>(body);
    }

    public static ResultData<CodeError> error(CodeError error, boolean isSuccess) {
        return new ResultData<>(error, isSuccess);
    }
}

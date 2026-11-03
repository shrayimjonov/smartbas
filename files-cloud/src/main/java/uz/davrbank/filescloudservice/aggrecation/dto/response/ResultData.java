package uz.davrbank.filescloudservice.aggrecation.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import uz.davrbank.filescloudservice.aggrecation.enums.ResponseStatus;
import uz.davrbank.filescloudservice.aggrecation.enums.ResultState;


@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
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

    public ResultData(ResultState resultState, String message) {
        this.successCode = -1;
        this.isSuccess = false;
        this.status = ResponseStatus.FAILED;
        this.error = new CoreError(resultState.getCode(), message);
    }

    public ResultData(CoreError coreError, Boolean isSuccess) {
        this.successCode = -1;
        this.isSuccess = false;
        this.status = ResponseStatus.FAILED;
        this.error = coreError;
    }

    public ResultData(ResultState resultState, Exception e) {
        this.successCode = -1;
        this.isSuccess = false;
        this.status = ResponseStatus.FAILED;
        this.error = new CoreError(resultState.getCode(), e.getMessage());
    }

    public static <T> ResultData<T> ok(@NotNull T body) {
        return new ResultData<>(body);
    }

    public static ResultData<CoreError> error(@NotNull ResultState resultState, @NotNull String message) {
        return new ResultData<>(resultState, message);
    }
}

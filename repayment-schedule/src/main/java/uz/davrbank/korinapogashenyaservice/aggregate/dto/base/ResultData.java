package uz.davrbank.korinapogashenyaservice.aggregate.dto.base;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import uz.davrbank.korinapogashenyaservice.aggregate.enums.ResponseStatus;

/**
 * Author: Shukhratjon Rayimjonov
 * Date: 22.01.2025
 * Time: 17:30
 * Project: korinapogasheniya
 */
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
    private CodeError error;
    private Long timeStamp = System.currentTimeMillis();
    private Integer count;

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
        this.error = new CodeError(code, e.getMessage());
    }

    public ResultData(CodeError coreError, boolean isSuccess) {
        this.successCode = -1;
        this.isSuccess = false;
        this.status = ResponseStatus.FAILED;
        this.error = coreError;
    }

    public static <T> ResultData<T> ok(@NotNull T body) {
        return new ResultData<>(body);
    }

    public static ResultData<CodeError> error(CodeError error, boolean isSuccess) {
        return new ResultData<>(error, isSuccess);
    }
}
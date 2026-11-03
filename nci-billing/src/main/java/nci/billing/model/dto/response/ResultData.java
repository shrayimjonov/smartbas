package nci.billing.model.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nci.billing.model.dto.ProblemDto;
import nci.billing.model.enums.ResponseCode;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResultData<T> {
    private Integer code = -1;
    private ResponseCode status = ResponseCode.FAILED_PAY;
    private T result;
    private ProblemDto error;

    public ResultData(T result) {
        this.code = 0;
        this.status = ResponseCode.SUCCESS;
        this.result = result;
    }

    public ResultData(ResponseCode status, String responseText) {
        this.code = status.getCode();
        this.status = status;
        this.error = ProblemDto.builder()
                .code(400)
                .message(responseText)
                .build();
    }

    public ResultData(int code, String errorMessage) {
        this.error = ProblemDto.builder()
                .code(code)
                .message(errorMessage)
                .build();
    }
}

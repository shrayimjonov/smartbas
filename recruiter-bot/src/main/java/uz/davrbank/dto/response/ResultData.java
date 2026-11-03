package uz.davrbank.dto.response;


import lombok.Getter;
import uz.davrbank.enums.ResponseStatus;


@Getter
public class ResultData<T> extends ResponseData {
    private T data;


    public ResultData(T data) {
        super(true);
        this.data = data;
    }

    public ResultData(Boolean status, ResponseStatus code, Exception ex) {
        super(status, code, ex.getMessage());
    }

    public ResultData(boolean status, ResponseStatus responseStatus, String message) {
        super(status, responseStatus, message);
    }

}

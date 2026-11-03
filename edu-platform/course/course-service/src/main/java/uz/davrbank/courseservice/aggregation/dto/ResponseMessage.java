package uz.davrbank.courseservice.aggregation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseMessage<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = -6282696650955489748L;
    private int status;
    private int code;
    private String message;
    private String description;
    private T data;

    public ResponseMessage(int status, int code, String message, String description) {
        this.status = status;
        this.code = code;
        this.message = message;
        this.description = description;
    }

    public ResponseMessage(T data, int code) {
        this.data = data;
        this.code = code;
    }
}
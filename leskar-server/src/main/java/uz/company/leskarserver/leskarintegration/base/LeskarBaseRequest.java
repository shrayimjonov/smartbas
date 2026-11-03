package uz.company.leskarserver.leskarintegration.base;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LeskarBaseRequest<T> implements Serializable {

    String signature;

    T payload;

    public LeskarBaseRequest(String signature, T payload) {
        this.signature = signature;
        this.payload = payload;
    }
}



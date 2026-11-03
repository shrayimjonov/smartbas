package uz.company.leskarserver.leskarintegration.base;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LeskarBaseResponse<T> implements Serializable {

    String signature;

    boolean state;

    String responseTime;

    T payload;
}

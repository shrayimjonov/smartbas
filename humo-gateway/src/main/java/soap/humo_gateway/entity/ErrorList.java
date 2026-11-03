package soap.humo_gateway.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "humo_error_list", indexes = {@Index(columnList = "code", name = "humo_error_code_ind")})
public class ErrorList {
    @SequenceGenerator(name = "hum_err_seq",
            sequenceName = "hum_err_seq",
            allocationSize = 1,
    initialValue = 27)
    @GeneratedValue(generator = "hum_err_seq")
    @Id
    private Long id;
    private String code;
    private String message;
}

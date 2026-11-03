package uz.davrbank.creditaccountsservice.aggregate.request;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class CreditRequestPageable extends CreditAccountRequestDTO implements Serializable {

    private Integer pageNumber;

    private Integer pageSize;
}

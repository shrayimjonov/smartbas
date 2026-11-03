package uz.davrbank.payment.model.property;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class BaseProperty {
    private String url;
    private Integer readTimeOut;
    private Integer connectTimeOut;
}

package uz.devops.humoMiddleware.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RateInfo {
    private String owner;
    private String rateSet;
    private String directQuotation;
    private String baseCurrency;
    private String counterCurrency;
    private String effectiveDate;
    private String sellRate;
    private String buyRate;
}

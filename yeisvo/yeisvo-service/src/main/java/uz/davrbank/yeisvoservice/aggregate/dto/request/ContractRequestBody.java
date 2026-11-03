package uz.davrbank.yeisvoservice.aggregate.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * request DTO for contract apis
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContractRequestBody {
    private String bankInn;
    private String bankId;
    private String idn;

}

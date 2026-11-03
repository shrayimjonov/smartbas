package uz.davrbank.yeisvoservice.aggregate.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ContractRequestParam { //api/contract/get
    private String signature;
    private Integer timestamp;
    private String serialNumber;
}

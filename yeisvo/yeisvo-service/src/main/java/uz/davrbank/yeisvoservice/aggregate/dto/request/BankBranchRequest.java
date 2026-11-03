package uz.davrbank.yeisvoservice.aggregate.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BankBranchRequest {
    private Long id;
    private String inn;
    private String mfo;
    private String name;
}

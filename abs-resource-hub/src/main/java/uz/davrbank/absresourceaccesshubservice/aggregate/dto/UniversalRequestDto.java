package uz.davrbank.absresourceaccesshubservice.aggregate.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UniversalRequestDto {
    private String procedureName;
    private String callStatement;
    private Object[] inParams;
}

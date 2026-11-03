package nci.billing.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nci.billing.model.annotation.Branch;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class DebtsDto {
    @Branch
    private String branch;
}

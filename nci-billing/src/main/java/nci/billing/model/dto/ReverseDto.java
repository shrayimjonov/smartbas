package nci.billing.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nci.billing.model.annotation.Branch;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReverseDto {
    @Branch(message = "bank branch")
    private String branch;
    @NotBlank
    @Pattern(regexp = "^\\d{11,13}$")
    private String generalId;
}

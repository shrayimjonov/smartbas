package uz.davrbank.payment.model.dto.core.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TranCheckRequestDTO {
    @NotBlank
    private String extId;
}

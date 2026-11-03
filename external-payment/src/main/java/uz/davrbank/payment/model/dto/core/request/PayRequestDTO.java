package uz.davrbank.payment.model.dto.core.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PayRequestDTO {
    @NotBlank
    private String id;
    @Min(100)
    @NotNull
    private BigDecimal amount = BigDecimal.valueOf(0);
    @NotBlank
    private String account;
    @NotBlank
    private String serviceId;
    @NotNull
    private UUID card;
}

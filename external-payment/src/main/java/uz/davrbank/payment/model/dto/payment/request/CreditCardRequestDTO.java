package uz.davrbank.payment.model.dto.payment.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreditCardRequestDTO {
    @NotBlank
    private String id;
    @Min(100)
    @NotNull
    private Long amount = 0L;
    @NotNull
    private UUID card;
}

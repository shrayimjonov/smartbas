package uz.davrbank.payment.model.dto.core.request;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CancelRequestDTO {

    @Min(1)
    private Long tranId = 0L;

}

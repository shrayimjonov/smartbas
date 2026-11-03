package uz.davrbank.bustreasury.aggregate.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PurposeResponseDTO {
    private Long id;
    private String purpose;
    private String code;
}
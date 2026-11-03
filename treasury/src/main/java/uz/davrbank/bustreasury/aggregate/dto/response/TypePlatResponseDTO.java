package uz.davrbank.bustreasury.aggregate.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TypePlatResponseDTO {
    private Long id;
    private String code;
    private String name;
}

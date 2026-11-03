package uz.davrbank.bustreasury.aggregate.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.davrbank.bustreasury.util.CurrencyType;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TemplateResponseDTO {
    private Long id;
    private String name;
    private String description;
    private CurrencyType currency;
    private List<WiringResponseDTO> wiring;
}

package uz.davrbank.bustreasury.aggregate.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TemplateRequestDTO {
    private String name;
    private String description;
}

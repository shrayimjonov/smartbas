package humo.integration.card.Model.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HumoVirtualCardsDto {
    @NotBlank
    @Pattern(regexp = "(00981)|(01046)|(01069)|(01072)|(01086)|(01121)|(01122)")
    private String branch;
    @NotBlank
    @Pattern(regexp = "\\d{8}")
    private String clientId;
}

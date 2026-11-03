package humo.integration.card.Model.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HumoVirtualDto {
    @NotBlank
    @Pattern(regexp = "(00981)|(01046)|(01069)|(01072)|(01086)|(01121)|(01122)")
    private String branch;
    @NotBlank
    @Pattern(regexp = "\\d{8}")
    private String clientId;
    @NotBlank
    @Pattern(regexp = "(^\\+998)\\d{9}")
    private String phone;
    @NotBlank
    @Pattern(regexp = "(22618\\d{12}2\\d{2})")
    private String account;
}

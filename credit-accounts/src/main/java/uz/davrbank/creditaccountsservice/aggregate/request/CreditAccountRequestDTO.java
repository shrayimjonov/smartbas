package uz.davrbank.creditaccountsservice.aggregate.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class CreditAccountRequestDTO implements Serializable {

    @NotBlank
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
    private String date;

    @NotBlank
    private String branch;

    private Integer state;

    @NotNull
    private Integer valType;

    private String account;
}

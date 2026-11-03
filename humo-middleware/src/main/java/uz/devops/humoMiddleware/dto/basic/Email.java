package uz.devops.humoMiddleware.dto.basic;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "Email")
public class Email {
    @NotNull
    @Size(max = 256)
    @ApiModelProperty("Email To address list separated by")
    private String to;
}

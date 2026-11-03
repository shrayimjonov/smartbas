package uz.davrbank.dto.request;


import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChangePasswordDto {

    @NotBlank
    private String username;

    @NotBlank
    private String oldPassword;

    @NotBlank
    private String newPassword;

    @NotBlank
    private String confirmNewPassword;

    public ChangePasswordDto fromRequest(ChangePasswordRequestDto request, String username) {
        this.username = username;
        this.oldPassword = request.getOldPassword();
        this.newPassword = request.getNewPassword();
        this.confirmNewPassword = request.getConfirmNewPassword();
        return this;
    }
}

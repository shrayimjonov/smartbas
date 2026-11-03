package uz.davrbank.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import lombok.*;


@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record AuthRequest(
        @NotBlank(message = "Username must not be blank")
        String username,
        @NotBlank(message = "Password must not be blank")
        String password) {
}

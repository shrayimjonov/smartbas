package uz.davrbank.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserRequestDto {
    private String name;
    @NotBlank(message = "Phone must not be blank")
    private String phone;
    @NotNull(message = "fileId must not be null")
    private Long fileId;
    @NotNull(message = "departmentId must not be null")
    private Long departmentId;
    @NotBlank(message = "language must not be blank")
    private String language;

    private LocalDateTime createdAt;

}

package uz.davrbank.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserTestRequestDto {
    @NotNull(message = "userId must not be null")
    private Long userId;
    @Valid
    @NotNull(message = "answers must not be null")
    private List<UserAnswerRequestDto> answers;
}

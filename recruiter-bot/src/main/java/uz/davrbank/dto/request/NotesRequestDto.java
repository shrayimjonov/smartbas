package uz.davrbank.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

/**
 * Email : user@example.com
 * Date :12/3/2024
 * Time :3:34 PM
 * Project name : recruiterbot
 */

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NotesRequestDto {
    @NotBlank(message = "notes must not be blank")
    private String notes;
}

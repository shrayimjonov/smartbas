package uz.davrbank.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import uz.davrbank.config.annotations.AtLeastOneNotNull;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@AtLeastOneNotNull(fields = {"uz", "ru"}, message = "At least one of 'uz' or 'ru' must be provided and not null")
public class QuestionTextDto {
    private String uz;
    private String ru;

}

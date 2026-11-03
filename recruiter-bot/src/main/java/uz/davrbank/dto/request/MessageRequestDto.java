package uz.davrbank.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
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
public class MessageRequestDto {
    @Valid
    @NotNull(message = "Title must not be null")
    private LocalizedTextDto title;
    @Valid
    @NotNull(message = "Description must not be null")
    private LocalizedTextDto description;
    private LocalDateTime createdAt;
    private LocalDateTime sendPostDate;
    private Integer messageId;
    private String sendTime;
    private Long photoId;


}

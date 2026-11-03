package uz.davrbank.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import uz.davrbank.entity.LocalizedText;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MessageResponse {
    private Long id;
    private LocalizedText title;
    private LocalizedText description;
    private LocalDateTime createdAt;
    private LocalDateTime sendPostDate;
    private Integer messageId;
    private Long photoId;
}

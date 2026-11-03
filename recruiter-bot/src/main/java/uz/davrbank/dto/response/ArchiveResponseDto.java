package uz.davrbank.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import uz.davrbank.entity.LocalizedText;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ArchiveResponseDto {
    private Long id;
    private LocalizedText title;
    private LocalizedText description;
    private Integer messageId;
    private LocalDateTime deletedDate;
}

package uz.davrbank.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Lob;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FileUploadResponse {
    private Long id;
    private String fileName;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @Lob
    private byte[] file;
    private LocalDateTime uploadedAt;
}

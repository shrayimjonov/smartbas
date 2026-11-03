package soap.humo_gateway.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErrorDto {
    private ErrorResponse error;
    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ErrorResponse{
        @Builder.Default
        private Integer code = 200;
        @Builder.Default
        private String message = "Successfully";
        private String details;
        @Builder.Default
        private LocalDateTime requestTime = LocalDateTime.now(ZoneId.of("Asia/Tashkent"));
    }


}

package uz.davrbank.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Email : user@example.com
 * Date :11/26/2024
 * Time :4:04 PM
 * Project name : recruiterbot
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserTestResponse {
    private LocalDateTime requestDate;
    private String month;
    private String totalScore;
    private String totalTests;
    private String percentage;
    private String status;
}

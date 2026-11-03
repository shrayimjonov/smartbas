package uz.davrbank.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserTestDetailResponse {
    private Long id;
    private String name;
    private String phone;
    private String month;
    private String departmentName;
    private LocalDateTime requestDate;
    private String attachedFile;
    private Long fileId;
    private String totalScore;
    private String totalTests;
    private String percentage;
    private String status;
    private String fileName;
    private String notes;
    private UserTestResponse testResponse;

}

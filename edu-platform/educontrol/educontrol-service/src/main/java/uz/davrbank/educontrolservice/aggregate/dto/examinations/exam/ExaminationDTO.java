package uz.davrbank.educontrolservice.aggregate.dto.examinations.exam;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExaminationDTO {

    private Long id;

    @NotNull(message = "Экзамена названия обязательно")
    @JsonProperty("examinationTitle")
    private String examinationTitle;

    @NotNull(message = "Время начала экзамена обязательно")
    @JsonProperty("startTime")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime startTime; // Global Time (Front fix)

    @NotNull(message = "Время окончания экзамена обязательно")
    @JsonProperty("endTime")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime endTime;

    @NotNull
    @JsonProperty("type")
    private TypeGeneral type; // course or library

    @NotNull(message = "status экзамена обязательно")
    @JsonProperty("status")
    private StatusCondition status;

    @NotNull
    @JsonProperty("courseId")
    private Long courseId; // Course id  by default 1L

    @NotNull
    @JsonProperty("cloudresourceId")
    private Long cloudresourceId; // Cloud Resource id by default 1L

    @NotNull
    @JsonProperty("departmentId")
    private Long departmentId;
}
package uz.davrbank.esudservice.aggregate.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.validation.Valid;
import lombok.*;
import uz.davrbank.esudservice.utils.serializers.CustomDateTimeDeserializer;
import uz.davrbank.esudservice.utils.serializers.CustomDateTimeSerializer;

import java.time.LocalDateTime;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(NON_NULL)
public abstract class BaseResponse<T> {

    /**
     * Foydali ma’lumot qismi
     */
    @Valid
    @JsonProperty("data")
    private T data;

    /**
     * Qo‘shimcha ma’lumot
     */
    @JsonProperty("message")
    private String message;

    /**
     * Javob qaytgan vaqt
     */
    @JsonProperty("timestamp")
    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    private LocalDateTime timestamp;
}

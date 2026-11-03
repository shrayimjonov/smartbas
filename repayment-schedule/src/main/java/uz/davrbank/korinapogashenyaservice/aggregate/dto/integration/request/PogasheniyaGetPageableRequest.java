package uz.davrbank.korinapogashenyaservice.aggregate.dto.integration.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PogasheniyaGetPageableRequest {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
    String repDate;

    Integer state;

    Integer pageNumber;

    Integer pageSize;
}
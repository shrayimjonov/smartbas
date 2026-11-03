package davr.yeisvo.dto.response.spravochniki;

import com.fasterxml.jackson.annotation.JsonProperty;
import davr.yeisvo.dto.response.BaseResponse;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DicContractSubjectsResponse extends BaseResponse {
    private Long gtkId;
    private List<Records> records;

    @Data
    public static class Records{
        private Long idNumber; // bu bizdan qaytishda beramiz
        private Long id;
        private String name;

        @JsonProperty("isDeleted")
        private Boolean isDeleted;
    }
}

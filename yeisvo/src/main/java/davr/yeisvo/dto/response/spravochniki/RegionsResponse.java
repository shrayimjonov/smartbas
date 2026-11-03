package davr.yeisvo.dto.response.spravochniki;

import com.fasterxml.jackson.annotation.JsonProperty;
import davr.yeisvo.dto.response.BaseResponse;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class RegionsResponse extends BaseResponse {
    private Long gtkId;
    private List<Regions> records;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Regions {
        private Long idNumber; //bu bizdan qaytarishda beramiz

        private String id;
        private String name;

        @JsonProperty("isDeleted")
        private Boolean isDeleted;
    }

}


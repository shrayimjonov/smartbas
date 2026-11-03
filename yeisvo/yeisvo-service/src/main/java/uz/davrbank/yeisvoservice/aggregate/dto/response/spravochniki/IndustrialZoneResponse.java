package uz.davrbank.yeisvoservice.aggregate.dto.response.spravochniki;

import com.fasterxml.jackson.annotation.JsonProperty;
import uz.davrbank.yeisvoservice.aggregate.dto.response.BaseResponse;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class IndustrialZoneResponse extends BaseResponse
{   private Long gtkId;
    private List<IndustrialZone> records;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class IndustrialZone {
        private Long idNumber;
        private Long id;
        private String name;

        @JsonProperty("isDeleted")
        private Boolean isDeleted;
    }

}

package uz.davrbank.userinfobase.dto.response.dochub;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class DochubResponse {

    @JsonProperty("jsonrpc")
    private String jsonrpc;

    @JsonProperty("result")
    private Result result;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    @Builder
    public static class Result {

        @JsonProperty("file_data")
        private String file_data;

        @JsonProperty("_typename")
        private String typename;
    }
}

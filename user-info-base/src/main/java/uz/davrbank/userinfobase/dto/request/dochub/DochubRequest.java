package uz.davrbank.userinfobase.dto.request.dochub;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class DochubRequest {
    @JsonProperty("json-rpc")
    private String jsonRpc = "2.0";
    @JsonProperty("method")
    private String method = "loadFile";
    @JsonProperty("service")
    private String service = "kk_dochub";
    @JsonProperty("params")
    private Params params;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Params {
        @JsonProperty("loanId")
        private String loanId;
        //        @JsonProperty("branch")
//        private String branch;
        @JsonProperty("file_name")
        private String file_name = "application.pdf";
    }
}
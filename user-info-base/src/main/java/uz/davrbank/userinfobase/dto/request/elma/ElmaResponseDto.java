package uz.davrbank.userinfobase.dto.request.elma;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ElmaResponseDto {

    @JsonProperty("status")
    private String status;

    @JsonProperty("data")
    private ElmaResponseDataDto data;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @ToString
    public static class ElmaResponseDataDto {

        @JsonProperty("action")
        private String action;

        @JsonProperty("method")
        private String method;

        @JsonProperty("server")
        private String server;

        @JsonProperty("messageContent")
        private MessageContentDto messageContent;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @ToString
    public static class MessageContentDto {

        @JsonProperty("LoanId")
        private String LoanId;

        @JsonProperty("branchId")
        private String branchId;
    }
}

package uz.davrbank.userinfobase.dto.request.elma;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;



@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ElmaRequestDto {


    @JsonProperty("action")
    private String action = "getLoanDoc";
    @JsonProperty("method")
    private String method = "getFile";
    @JsonProperty("server")
    private String server = "elma";

    @JsonProperty("InstanceID")
    private String InstanceID = "";
    @JsonProperty("TransactionRequestID")
    private String TransactionRequestID = "";

    @JsonProperty("ErrorMessage")
    private String ErrorMessage = "";
    @JsonProperty("messageContent")
    private MessageContentDto messageContent;
    @JsonProperty("Success")
    private String Success = "1";
    @JsonProperty("MessageType")
    private String MessageType = "DataResponse";
    @JsonProperty("RetryRequestId")
    private String RetryRequestId = "";
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

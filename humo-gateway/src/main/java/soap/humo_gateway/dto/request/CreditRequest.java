package soap.humo_gateway.dto.request;

import lombok.Data;
import soap.humo_gateway.dto.annotations.HumoRegex;
import soap.humo_gateway.dto.enums.SubSystem;
import soap.humo_gateway.dto.annotations.CheckSubSystem;
import soap.humo_gateway.dto.annotations.PassDataNotNull;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@PassDataNotNull(message = "check credit card person info, when transaction more than 10 million sum")
public class CreditRequest {
    @NotBlank
    private String refNum;
    @NotBlank
    @HumoRegex
    private String pan2;
    @NotBlank
    @Pattern(regexp = "(2\\d{3})|(3\\d{3})")
    private String expiry;
    @Min(1)
    private Long amount = 0L;
    @NotBlank
    private String merchantId;
    @NotBlank
    private String terminalId;
    @NotBlank
    @Pattern(regexp = "(wsdavr)|(wsdavr2)")
    private String login;
    @Valid
    private Params params;
    @CheckSubSystem(message = "subsystem")
    private SubSystem subSystem;

    @Data
    public static class Params {
        @NotBlank
        @Size(min = 3, max = 3)
        private String senderNationality;
        @NotBlank
        @Size(min = 3, max = 3)
        private String senderDocType;
        @NotBlank
        @Size(min = 2, max = 2)
        private String senderSerialNo;
        @NotBlank
        @Size(min = 7)
        private String senderIdCard;
        @NotBlank
        @Pattern(regexp = "^((19|2[0-9])[0-9]{2})-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])$")
        private String senderDocValidThrough;
        @NotBlank
        @Size(min = 14, max = 14)
        private String senderPersonCode;
        @NotBlank
        @Size(min = 8)
        private String senderSurname;
        @NotBlank
        @Size(min = 5)
        private String senderFirstName;
        @NotBlank
        @Size(min = 8)
        private String senderMiddleName;
        @NotBlank
        @Pattern(regexp = "^((19|2[0-9])[0-9]{2})-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])$")
        private String senderBirthDate;
    }
}






package uz.davrbank.esudservice.aggregate.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import jakarta.validation.constraints.NotNull;

/**
 * API foydalanuvchiga qaytariladigan umumiy javob modeli.
 */
@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InvoiceResponseDto extends BaseResponse<InvoiceResponseDto.InvoiceData> {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class InvoiceData {

        /** Invoice raqami */
        @JsonProperty("invoiceNumber")
        @NotNull(message = "invoiceNumber must not be null")
        private String invoiceNumber;

        /** Invoice yaratilgan sana */
        @JsonProperty("issuedDate")
        @NotNull(message = "issuedDate must not be null")
        private String issuedDate;
    }
}

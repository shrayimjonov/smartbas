package uz.davrbank.esudservice.aggregate.dto.reuqest;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import uz.davrbank.esudservice.dto.out.IndividualPersonDtoForInvoice;
import uz.davrbank.esudservice.dto.out.LegalEntityDtoForInvoice;
import uz.davrbank.esudservice.enums.EntityType;

import java.util.UUID;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(NON_NULL)
public class InvoiceGenerateRequestDto {

    /**
     * Kvitansiya summasi so’mlarda
     * Example: "300000.00"
     */
    @JsonProperty("amount")
    @NotBlank(message = "amount must not be blank")
    private String amount;

    /**
     * Kvitansiya harajati turi
     * Dictionary: /amount-types
     */
    @JsonProperty("amountType")
    @NotBlank(message = "amountType must not be blank")
    private String amountType;

    /**
        * Toʻlovchi shaxs turi
     */
    @JsonProperty("entityType")
    @NotNull(message = "entityType must not be null")
    private EntityType entityType;

    /**
     * Toʻlovchi shaxs ma’lumotlari.
     */
    @Valid
    @JsonProperty("individualEntity")
    @NotNull(message = "individualEntity must not be null when entityType is INDIVIDUAL")
    private IndividualPersonDtoForInvoice individualEntity;

    /**
     * To’lovchi yuridik shaxs ma’lumotlari
     */
    @Valid
    @JsonProperty("legalEntity")
    @NotNull(message = "legalEntity must not be null when entityType is LEGAL")
    private LegalEntityDtoForInvoice legalEntity;

    /**
     * Sud turi
     */
    @JsonProperty("courtType")
    @NotBlank(message = "courtType must not be blank")
    private String courtType;

    /**
     * Sud identifikatori
     */
    @JsonProperty("courtOwnId")
    @NotNull(message = "courtOwnId must not be null")
    private UUID courtOwnId;
}

package uz.davrbank.esudservice.aggregate.dto.reuqest;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import uz.davrbank.esudservice.dto.out.*;

import java.util.List;
import java.util.UUID;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(NON_NULL)
public class RegisterCaseRequestDto {

    /**
     * Hujjatning ish yuboruvchi tomondagi identifikatori (Davr Bank tizimidagi caseId)
     */
    @JsonProperty("electronCaseId")
    @NotNull(message = "electron case id must not be null")
    private UUID electronCaseId;

    /**
     * Ish (ariza)
     */
    @Valid
    @JsonProperty("case")
    @NotNull(message = "case object must not be null")
    private CaseDto caseDto;

    /**
     * Ish turkumlari
     */
    @Valid
    @JsonProperty("claimCategories")
    @Size(min = 1, message = "claim categories must not be empty")
    private List<ClaimCategoryDto> claimCategories;

    /**
     * Kvitansiyalar
     */
    @Valid
    @JsonProperty("receipts")
    @Size(min = 1, message = "receipts must not be empty")
    private List<ReceiptDto> receipts;

    /**
     * Ish hujjatlari
     */
    @Valid
    @JsonProperty("caseDocuments")
    @Size(min = 1, message = "case documents must not be empty")
    private List<CaseDocumentDto> caseDocuments;

    /**
     * Ishtirokchilar
     */
    @Valid
    @JsonProperty("participants")
    @Size(min = 1, message = "case participants must not be empty")
    private List<CaseParticipant> participants;

    /**
     * Daʼvo summalari
     */
    @Valid
    @JsonProperty("claimAmountWithParts")
    @Size(min = 1, message = "claim amount with parts object must not be empty")
    private List<ClaimAmountsWithParts> claimAmountsWithParts;

    /**
     * Ish daʼvo
     */
    @Valid
    @JsonProperty("claim")
    @NotNull(message = "claim object must not be null")
    private ClaimDto claim;

    @JsonProperty("certificate")
    @NotEmpty(message = "certificate must not be null")
    private String certificate;

    @JsonProperty("signature")
    @NotEmpty(message = "signature must not be null")
    private String signature;

}

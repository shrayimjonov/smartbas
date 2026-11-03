package uz.davrbank.bustreasury.aggregate.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WiringResponseDTO {
    private Long id;
    private Long wiringId;
    private MFOResponseDTO debitMFO;
    private String debitAccountNum;
    private String debitInn;
    private String debitComment;
    private MFOResponseDTO creditMFO;
    private String creditAccountNum;
    private String creditInn;
    private String creditComment;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private LocalDate date;
    private PurposeResponseDTO purpose;
    private DocTypeResponseDTO docType;
    private TemplateResponseDTO template;
    private PaymentResponseDTO payment;
    private TypePlatResponseDTO typePlate;
    private String createdBy;
    private String transId;
    private String documentNumber;
}

package uz.davrbank.payment.model.dto.tran.request.params;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.davrbank.payment.model.enums.Nationality;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TranCreditPersonParam {
    private String personCode;
    private String firstName;
    private String surname;
    private String patronymic;
    private Nationality nationality;
    private String passportSerial;
    private String passportNumber;
    private LocalDate birthDate;
    private LocalDate passportExpiryDate;
}

package soap.humo_gateway.dto.annotations;

import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.beans.factory.annotation.Value;
import soap.humo_gateway.dto.request.CreditRequest;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Slf4j
public class PassDataValidator implements ConstraintValidator<PassDataNotNull, Object> {
    @Value("${properties.max_amount}")
    private Long maxAmount;

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext ctx) {


        var creditRequest = (CreditRequest) value;
        log.info("Card: {} replenishment request sum: {} and max amount: {}",
                creditRequest.getPan2(),
                creditRequest.getAmount(),
                maxAmount);

        if (creditRequest.getAmount() >= maxAmount) {
            log.info("requested amount: {} more than maxAmount and will be taken passport Data", creditRequest.getAmount());
            return creditRequest.getParams() != null
                    && creditRequest.getParams().getSenderIdCard() != null
                    && creditRequest.getParams().getSenderSerialNo() != null
                    && creditRequest.getParams().getSenderSurname() != null
                    && creditRequest.getParams().getSenderFirstName() != null
                    && creditRequest.getParams().getSenderMiddleName() != null
                    && creditRequest.getParams().getSenderNationality() != null
                    && creditRequest.getParams().getSenderDocType() != null
                    && creditRequest.getParams().getSenderDocValidThrough() != null
                    && creditRequest.getParams().getSenderBirthDate() != null
                    && creditRequest.getParams().getSenderPersonCode() != null;
        }
        return true;
    }
}

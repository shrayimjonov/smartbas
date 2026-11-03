package nci.billing.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nci.billing.entity.Fraud;
import nci.billing.exception.FraudStopper;
import nci.billing.model.dto.PayDto;
import nci.billing.model.request.PayRequest;
import nci.billing.service.FraudService;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

@Component
@RequiredArgsConstructor
@Slf4j
public class FraudDetection {
    private final FraudService fraudService;


    public Fraud isFraudulentPayment(PayRequest request) {


        if (!fraudService.getFraudByPaymentId(request.getPaymentId()).isEmpty()) {
            fraudService.createNewFraud(request, true, "Duplicate paymentId %s for loan %s".formatted(request.getPaymentId(), request.getLoanId()));
            throw new FraudStopper("Duplicate paymentId %s for loan %s".formatted(request.getPaymentId(), request.getLoanId()));
        }


        int count = 0;

        var loanPaymentList = fraudService.getDailyFraudByLoanId(request.getLoanId());

        var newFraud = fraudService.createNewFraud(request, false, "SUCCESS");

        if (loanPaymentList.isEmpty()) {
            log.info("Daily fraud for loan : {} is empty!", request.getLoanId());
            return newFraud;
        }


        if (loanPaymentList.stream().anyMatch(fraud -> fraud.getIsFraudster().equals(true))) {
            updateFraudToFraudulentIsTrue(newFraud.getId(), true, "PaymentId %s for loan %s is fraud. Cause before this transaction has transactions with fraud status".formatted(request.getPaymentId(), request.getLoanId()));
            throw new FraudStopper(("PaymentId %s for loan %s is fraud. Cause before this transaction has transactions with fraud status")
                    .formatted(request.getPaymentId(), request.getLoanId()));
        }


        log.info("Start checking loan : {} payments", request.getLoanId());

        for (var loan : loanPaymentList) {

            var nowTime = ZonedDateTime.now();

            var differenceDaysInLong = loan.getCreatedTime().until(nowTime, ChronoUnit.DAYS);
            log.info("Fraud days difference:  {} ", differenceDaysInLong);

            if (differenceDaysInLong <= 1) {
                var differenceHoursInLong = loan.getCreatedTime().until(nowTime, ChronoUnit.HOURS);
                log.info("Fraud hours difference:  {} ", differenceHoursInLong);

                if (differenceHoursInLong == 0)
                    count++;
            }


        }

        log.info("Fraud count is : {}", count);

        if (count >= 7) {
            updateFraudToFraudulentIsTrue(newFraud.getId(), true, "PaymentId %s in Loan %s detected more than 4 times in 1 hour".formatted(request.getPaymentId(), request.getLoanId()));
            throw new FraudStopper("PaymentId %s in Loan %s detected more than 4 times in 1 hour".formatted(request.getPaymentId(), request.getLoanId()));
        }

        return newFraud;
    }


    public void updateFraudToFraudulentIsTrue(Long id, Boolean isFraud, String reason) {
        var fraud = fraudService.findById(id);
        fraud.setReason(reason);
        fraud.setIsFraudster(isFraud);
        fraudService.update(fraud);
    }


}

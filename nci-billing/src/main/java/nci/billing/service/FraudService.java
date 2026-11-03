package nci.billing.service;

import nci.billing.entity.Fraud;
import nci.billing.model.dto.PayDto;
import nci.billing.model.request.PayRequest;

import java.util.List;

public interface FraudService {
    Fraud findById(Long id);
    List<Fraud> getDailyFraudByLoanId(String loanId);
    void update(Fraud fraud);
    Fraud createNewFraud(PayRequest request, Boolean isFraudster, String reason);
    List<Fraud> getFraudByPaymentId(String paymentId);


}

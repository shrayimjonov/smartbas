package nci.billing.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nci.billing.entity.Fraud;
import nci.billing.exception.EntityNotFound;
import nci.billing.model.dto.PayDto;
import nci.billing.model.request.PayRequest;
import nci.billing.repository.FraudRepository;
import nci.billing.service.FraudService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FraudImpl implements FraudService {
    private final FraudRepository repository;



    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Fraud createNewFraud(PayRequest request, Boolean isFraudster, String reason) {
        var fraud = prepareFraud(request, isFraudster, reason);
        log.info("creating new Fraud: {}", fraud);
      return repository.saveAndFlush(fraud);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Fraud> getFraudByPaymentId(String paymentId) {
        return repository.getFraudByPaymentId(paymentId);
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public List<Fraud> getDailyFraudByLoanId(String loanId) {
        return repository.getFraudByLoan(loanId);
    }

    @Override
    @Transactional
    public void update(Fraud fraud) {
        repository.save(fraud);
    }


    @Override
    @Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
    public Fraud findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFound("Fraud with id: %d not found in Database".formatted(id)));
    }


    private Fraud prepareFraud(PayRequest request, Boolean isFraudster, String reason) {
        return new Fraud(isFraudster, request.getLoanId(), request.getPaymentId(), request.getAmount(), request.getUsername(), reason);
    }

}

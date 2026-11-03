package nci.billing.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nci.billing.exception.TimeOut;
import nci.billing.feign.LoanFeign;
import nci.billing.model.response.loan.LoanInfoByPINFLResponse;
import nci.billing.model.response.loan.LoanInfoResponse;
import nci.billing.service.FeignService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class FeignImpl implements FeignService {
    private final LoanFeign loanFeign;


    public LoanInfoByPINFLResponse getLoanInfoByPINFL(String pinfl) {
        try {
            return loanFeign.getLoanInfoByPINFL(pinfl);
        } catch (Exception e) {
            throw new TimeOut(e.getMessage());
        }
    }

    public LoanInfoResponse getLoanInfo(String branch,
                                        Integer loanId) {
        try {
            return loanFeign.getLoanInfo(branch, loanId);
        } catch (Exception e) {
            throw new TimeOut(e.getMessage());
        }
    }

}

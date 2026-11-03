package nci.billing.service;

import nci.billing.model.dto.PayDtoV2;
import nci.billing.model.dto.response.ResultData;

public interface CoreV2Service {
    ResultData<?> pay(PayDtoV2 dto);

    ResultData<?> check(String paymentId);
}

package nci.billing.service;

import nci.billing.entity.Reverse;
import nci.billing.model.enums.PayStatus;

import java.util.Map;

public interface ReverseService {
    Reverse create(Map<String,String> params);

    void update(Long id, String response, PayStatus status);

    Reverse findById(Long id);


}

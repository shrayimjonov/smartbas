package uz.davrbank.payment.service.core;

import uz.davrbank.payment.model.entity.Bill;

public interface BillService {
    Bill save(Bill bill);


    Bill findByTranId(Long tranId);
}

package uz.davrbank.bustreasury.service.interfaces;

import uz.davrbank.bustreasury.aggregate.dto.response.cbu.CurrencyRateResponseDTO;


public interface CBUCallerInterface {
    CurrencyRateResponseDTO[] getLatestRateUpdate();
}

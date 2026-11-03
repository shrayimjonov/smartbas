package uz.davrbank.bustreasury.service.interfaces;

import jakarta.validation.Valid;
import uz.davrbank.bustreasury.aggregate.dto.request.ExchangeRequestDTO;
import uz.davrbank.bustreasury.aggregate.dto.request.TreasuryRequestDTO;
import uz.davrbank.bustreasury.aggregate.dto.response.ResultDTO;


public interface TreasuryInterface {
    ResultDTO createTreasury(TreasuryRequestDTO requestDTO);

    ResultDTO createTreasuryExchange(@Valid ExchangeRequestDTO requestDTO);
}

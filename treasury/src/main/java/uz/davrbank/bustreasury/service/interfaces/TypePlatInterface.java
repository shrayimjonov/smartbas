package uz.davrbank.bustreasury.service.interfaces;

import uz.davrbank.bustreasury.aggregate.dto.request.TypePlatRequestDTO;
import uz.davrbank.bustreasury.aggregate.dto.response.TypePlatResponseDTO;

import java.util.List;

public interface TypePlatInterface {
    TypePlatResponseDTO createPayment(TypePlatRequestDTO requestDTO);

    TypePlatResponseDTO getPaymentById(Long id);

    List<TypePlatResponseDTO> getAllPayments();
}

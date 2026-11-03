package uz.davrbank.bustreasury.service.interfaces;

import kotlin.text.UStringsKt;
import uz.davrbank.bustreasury.aggregate.dto.request.PaymentRequestDTO;
import uz.davrbank.bustreasury.aggregate.dto.response.PaymentResponseDTO;

import java.util.List;


public interface PaymentInterface {
    PaymentResponseDTO createPayment(PaymentRequestDTO requestDTO);

    PaymentResponseDTO getPaymentById(Long id);

    List<PaymentResponseDTO> getAllPayments();

    String deleteById(Long id);
}

package uz.davrbank.bustreasury.service.cases;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import uz.davrbank.bustreasury.aggregate.dto.request.PaymentRequestDTO;
import uz.davrbank.bustreasury.aggregate.dto.response.PaymentResponseDTO;
import uz.davrbank.bustreasury.aggregate.model.Payment;
import uz.davrbank.bustreasury.repository.PaymentRepository;
import uz.davrbank.bustreasury.repository.user.SentWiringRepository;
import uz.davrbank.bustreasury.service.interfaces.PaymentInterface;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaymentService implements PaymentInterface {

    private final PaymentRepository paymentRepository;
    private final SentWiringRepository sentWiringsRepository;
    public PaymentService(PaymentRepository paymentRepository, SentWiringRepository sentWiringsRepository) {
        this.paymentRepository = paymentRepository;
        this.sentWiringsRepository = sentWiringsRepository;
    }


    @Override
    public PaymentResponseDTO createPayment(PaymentRequestDTO requestDTO) {
        Payment payment = new Payment();
        payment.setName(requestDTO.getName());
        payment.setCode(requestDTO.getCode());
        paymentRepository.save(payment);
        return new PaymentResponseDTO(
                payment.getId(),
                payment.getCode(),
                payment.getName()
        );
    }

    @Override
    public PaymentResponseDTO getPaymentById(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Payment not found with code: " + id));
        return new PaymentResponseDTO(
                payment.getId(),
                payment.getCode(),
                payment.getName()
        );
    }

    @Override
    public List<PaymentResponseDTO> getAllPayments() {
        return paymentRepository.findAll().stream()
                .map(payment -> new PaymentResponseDTO(
                        payment.getId(),
                        payment.getCode(),
                        payment.getName()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public String deleteById(Long id) {
        // Find the payment
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Payment with ID " + id + " not found"));

        // Check if the payment is referenced in sent_wirings
        if (sentWiringsRepository.existsByPaymentId(id)) {
            throw new DataIntegrityViolationException(
                    String.format("Cannot delete Payment with ID %d and name %s because it is referenced in sent_wirings",
                            id, payment.getName())
            );
        }

        // Delete the payment
        paymentRepository.deleteById(id);

        // Return success message
        return String.format("Payment with ID %d and name %s deleted successfully", id, payment.getName());
    }
}

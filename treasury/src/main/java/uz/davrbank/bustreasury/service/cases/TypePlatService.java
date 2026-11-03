package uz.davrbank.bustreasury.service.cases;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import uz.davrbank.bustreasury.aggregate.dto.request.TypePlatRequestDTO;
import uz.davrbank.bustreasury.aggregate.dto.response.TypePlatResponseDTO;
import uz.davrbank.bustreasury.aggregate.model.TypePlat;
import uz.davrbank.bustreasury.repository.TypePlatRepository;
import uz.davrbank.bustreasury.service.interfaces.TypePlatInterface;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TypePlatService implements TypePlatInterface {
    private final TypePlatRepository typePlateRepository;

    public TypePlatService(TypePlatRepository typePlateRepository) {
        this.typePlateRepository = typePlateRepository;
    }

    @Override
    public TypePlatResponseDTO createPayment(TypePlatRequestDTO requestDTO) {
        TypePlat typePlate = new TypePlat();

        typePlate.setName(requestDTO.getName());
        typePlate.setCode(requestDTO.getCode());
        typePlateRepository.save(typePlate);
        return new TypePlatResponseDTO(
                typePlate.getId(),
                typePlate.getCode(),
                typePlate.getName()
        );
    }

    @Override
    public TypePlatResponseDTO getPaymentById(Long id) {
        TypePlat typePlate = typePlateRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("Payment not found with code: " + id));
        return new TypePlatResponseDTO(
                typePlate.getId(),
                typePlate.getCode(),
                typePlate.getName()
        );

    }

    @Override
    public List<TypePlatResponseDTO> getAllPayments() {
        return typePlateRepository.findAll().stream()
                .map(typePlate -> new TypePlatResponseDTO(
                        typePlate.getId(),
                        typePlate.getCode(),
                        typePlate.getName()
                ))
                .collect(Collectors.toList());
    }
}

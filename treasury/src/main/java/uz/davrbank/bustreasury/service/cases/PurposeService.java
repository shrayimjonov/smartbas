package uz.davrbank.bustreasury.service.cases;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import uz.davrbank.bustreasury.aggregate.dto.request.PurposeRequestDTO;
import uz.davrbank.bustreasury.aggregate.dto.response.PurposeResponseDTO;
import uz.davrbank.bustreasury.aggregate.mapper.PurposeMapper;
import uz.davrbank.bustreasury.aggregate.model.Purpose;
import uz.davrbank.bustreasury.exeption.AlreadyExistException;
import uz.davrbank.bustreasury.exeption.DataNotFoundException;
import uz.davrbank.bustreasury.repository.PurposeRepository;
import uz.davrbank.bustreasury.service.interfaces.PurposeInterface;

import java.util.List;
import java.util.Optional;

@Service
public class PurposeService implements PurposeInterface {
    private static final Logger LOG = LoggerFactory.getLogger(PurposeService.class);
    private final PurposeRepository purposeRepository;

    public PurposeService(PurposeRepository purposeRepository) {
        this.purposeRepository = purposeRepository;
    }

    @Override
    public PurposeResponseDTO createPurpose(PurposeRequestDTO requestDTO) {
        if (purposeRepository.existsByCode(requestDTO.getCode())) {
            throw new AlreadyExistException("Purpose with code " + requestDTO.getCode() + " already exist");
        }
        Purpose purpose = PurposeMapper.INSTANCE.purposeDTOToPurpose(requestDTO);
        purposeRepository.save(purpose);

        return PurposeMapper.INSTANCE.purposeToPurposeDTO(purpose);
    }

    @Override
    public List<PurposeResponseDTO> getAll() {
        return purposeRepository.findAll()
                .stream()
                .map(PurposeMapper.INSTANCE::purposeToPurposeDTO)
                .toList();
    }

    @Override
    public PurposeResponseDTO update(Long id, PurposeRequestDTO requestDTO) {
        Optional<Purpose> byId = purposeRepository.findById(id);
        if (byId.isEmpty()) {
            throw new DataNotFoundException("Purpose with id " + id + " not found");
        }
        Purpose purpose = byId.get();
        if (purposeRepository.existsByCode(requestDTO.getCode())) {
            throw new AlreadyExistException("Purpose with code " + requestDTO.getCode() + " already exist");
        }
        PurposeMapper.INSTANCE.updatePurposeFromDTO(requestDTO, purpose);
        purposeRepository.save(purpose);
        return PurposeMapper.INSTANCE.purposeToPurposeDTO(purpose);
    }

    @Override
    public String delete(Long id) {
        Optional<Purpose> byId = purposeRepository.findById(id);
        if (byId.isEmpty()) {
            throw new DataNotFoundException("Purpose with id " + id + " not found");
        }
        Purpose purpose = byId.get();
        purposeRepository.delete(purpose);
        return "Purpose with id " + id + " deleted";
    }

    @Override
    public PurposeResponseDTO getById(Long id) {
        Optional<Purpose> byId = purposeRepository.findById(id);
        if (byId.isEmpty()) {
            throw new DataNotFoundException("Purpose with id " + id + " not found");
        }
        Purpose purpose = byId.get();
        return PurposeMapper.INSTANCE.purposeToPurposeDTO(purpose);
    }
}

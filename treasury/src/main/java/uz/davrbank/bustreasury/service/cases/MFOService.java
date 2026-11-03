package uz.davrbank.bustreasury.service.cases;

import org.springframework.stereotype.Service;
import uz.davrbank.bustreasury.aggregate.dto.request.MFORequestDTO;
import uz.davrbank.bustreasury.aggregate.dto.response.MFOResponseDTO;
import uz.davrbank.bustreasury.aggregate.model.MFO;
import uz.davrbank.bustreasury.exeption.NotFoundException;
import uz.davrbank.bustreasury.repository.MFORepository;
import uz.davrbank.bustreasury.service.interfaces.MFOInterface;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MFOService implements MFOInterface {

    private final MFORepository mfoRepository;

    public MFOService(MFORepository mfoRepository) {
        this.mfoRepository = mfoRepository;
    }


    @Override
    public MFOResponseDTO createMFO(MFORequestDTO requestDTO) {
        MFO mfo = new MFO();
        mfo.setName(requestDTO.getName());
        mfo.setCode(requestDTO.getCode());
        mfoRepository.save(mfo);
        return new MFOResponseDTO(
                mfo.getId(),
                mfo.getCode(),
                mfo.getName()
        );
    }

    @Override
    public MFOResponseDTO getMFOById(Long id) {
        MFO mfo = mfoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("MFO not found with code: " + id));
        return new MFOResponseDTO(
                mfo.getId(),
                mfo.getCode(),
                mfo.getName()
        );
    }

    @Override
    public List<MFOResponseDTO> getAllMFOs() {
        return mfoRepository.findAll().stream()
                .map(mfo -> new MFOResponseDTO(
                        mfo.getId(),
                        mfo.getCode(),
                        mfo.getName()
                ))
                .collect(Collectors.toList());
    }
}

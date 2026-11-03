package uz.davrbank.bustreasury.service.cases;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import uz.davrbank.bustreasury.aggregate.dto.request.DocTypeRequestDTO;
import uz.davrbank.bustreasury.aggregate.dto.response.DocTypeResponseDTO;
import uz.davrbank.bustreasury.aggregate.model.DocType;
import uz.davrbank.bustreasury.exeption.AlreadyExistException;
import uz.davrbank.bustreasury.exeption.NotFoundException;
import uz.davrbank.bustreasury.repository.DocTypeRepository;
import uz.davrbank.bustreasury.service.interfaces.DocTypeInterface;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DocTypeService implements DocTypeInterface {

    private final DocTypeRepository docTypeRepository;

    public DocTypeService(DocTypeRepository docTypeRepository) {
        this.docTypeRepository = docTypeRepository;
    }

    @Override
    public DocTypeResponseDTO createDocType(DocTypeRequestDTO requestDTO) {
        if (docTypeRepository.existsByKodDoc(requestDTO.getKodDoc())) {
            throw new AlreadyExistException("DocType with ID " + requestDTO.getKodDoc() + " already exists");
        }

        DocType docType = new DocType();
        return getDocTypeResponseDTO(requestDTO, docType);
    }

    @Override
    public DocTypeResponseDTO getDocTypeById(Long id) {
        DocType docType = docTypeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("DocType not found with id: " + id));
        return new DocTypeResponseDTO(
                docType.getId(),
                docType.getKodDoc(),
                docType.getNaimDoc(),
                docType.getDateOpen(),
                docType.getDateClose(),
                docType.getAct()
        );
    }

    @Override
    public List<DocTypeResponseDTO> getAllDocTypes() {
        return docTypeRepository.findAll().stream()
                .map(docType -> new DocTypeResponseDTO(
                        docType.getId(),
                        docType.getKodDoc(),
                        docType.getNaimDoc(),
                        docType.getDateOpen(),
                        docType.getDateClose(),
                        docType.getAct()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public DocTypeResponseDTO updateDocType(Long id, DocTypeRequestDTO requestDTO) {
        DocType docType = docTypeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("DocType not found with ID: " + id));

        if (!docType.getKodDoc().equals(requestDTO.getKodDoc()) &&
            docTypeRepository.existsByKodDoc(requestDTO.getKodDoc())) {
            throw new AlreadyExistException("DocType with ID " + requestDTO.getKodDoc() + " already exists");
        }

        return getDocTypeResponseDTO(requestDTO, docType);
    }

    @NotNull
    private DocTypeResponseDTO getDocTypeResponseDTO(DocTypeRequestDTO requestDTO, DocType docType) {
        docType.setKodDoc(requestDTO.getKodDoc());
        docType.setNaimDoc(requestDTO.getNaimDoc());
        docType.setDateOpen(requestDTO.getDateOpen());
        docType.setDateClose(requestDTO.getDateClose());
        docType.setAct(requestDTO.getAct());

        docType = docTypeRepository.save(docType);

        return new DocTypeResponseDTO(
                docType.getId(),
                docType.getKodDoc(),
                docType.getNaimDoc(),
                docType.getDateOpen(),
                docType.getDateClose(),
                docType.getAct()
        );
    }

    @Override
    public void deleteDocType(Long id) {
        if (!docTypeRepository.existsById(id)) {
            throw new NotFoundException("DocType not found with ID: " + id);
        }
        docTypeRepository.deleteById(id);
    }
}

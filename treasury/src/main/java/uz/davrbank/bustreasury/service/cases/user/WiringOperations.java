package uz.davrbank.bustreasury.service.cases.user;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import uz.davrbank.bustreasury.aggregate.dto.response.user.OperationWithWiringDTO;
import uz.davrbank.bustreasury.aggregate.dto.response.user.SentWiringDTO;
import uz.davrbank.bustreasury.aggregate.model.Template;
import uz.davrbank.bustreasury.aggregate.model.user.Operations;
import uz.davrbank.bustreasury.aggregate.model.user.SentWiring;
import uz.davrbank.bustreasury.repository.TemplateRepository;
import uz.davrbank.bustreasury.repository.user.OperationsRepository;
import uz.davrbank.bustreasury.repository.user.SentWiringRepository;
import uz.davrbank.bustreasury.service.cases.user.rubl.WiringServiceUSERBuyRUB;
import uz.davrbank.bustreasury.util.CurrencyType;
import uz.davrbank.bustreasury.util.OperationType;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class WiringOperations implements uz.davrbank.bustreasury.service.interfaces.user.WiringOperations {

    private final SentWiringRepository sentWiringRepository;
    private final OperationsRepository operationsRepository;
    private final TemplateRepository templateRepository;
    private static final Logger log = LoggerFactory.getLogger(WiringOperations.class);


    public WiringOperations(SentWiringRepository sentWiringRepository, OperationsRepository operationsRepository, TemplateRepository templateRepository) {
        this.sentWiringRepository = sentWiringRepository;
        this.operationsRepository = operationsRepository;
        this.templateRepository = templateRepository;
    }

    @Override
    public List<SentWiring> getAll() {
        return sentWiringRepository.findAll(Sort.by(Sort.Direction.DESC, "createdDate"));
    }

    @Override
    public SentWiring getById(Long id) {
        return sentWiringRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("SentWiring not found: " + id));
    }

    @Override
    public List<SentWiring> getByTransId(String transId) {
        return sentWiringRepository.findByTransId(transId);
    }

    @Override
    @Transactional
    public OperationWithWiringDTO getOneLatestPerWiring(Long operationId) {
        Operations op = operationsRepository.findById(operationId)
                .orElseThrow(() -> new IllegalArgumentException("Operation not found: " + operationId));

        List<SentWiring> rows = sentWiringRepository
                .findAllByOperations_IdOrderByCreatedDateAsc(operationId);

        Map<Long, SentWiring> latestByWiring = rows.stream()
                .filter(s -> s.getWiring() != null && s.getWiring().getWiringId() != null)
                .collect(Collectors.toMap(
                        s -> s.getWiring().getWiringId(),
                        s -> s,
                        (a, b) -> a.getCreatedDate().isAfter(b.getCreatedDate()) ? a : b,
                        java.util.TreeMap::new
                ));

        List<SentWiringDTO> wires = latestByWiring.values().stream()
                .map(SentWiringDTO::fromEntity)
                .toList();

        return OperationWithWiringDTO.of(op, wires);
    }

    @Override
    @Transactional
    public Page<OperationWithWiringDTO> getAllWithWires(Pageable pageable) {
        Page<Operations> operationsPage = operationsRepository
                .findAllByOrderByLatestWiringDateDesc(pageable);

        return operationsPage.map(this::mapOpWithWires);
    }

    @Override
    public OperationWithWiringDTO getOneWithWiresByOperationId(Long operationId) {
        Operations op = operationsRepository.findById(operationId)
                .orElseThrow(() -> new IllegalArgumentException("Operation not found: " + operationId));
        return mapOpWithWires(op);
    }

    @Override
    @Transactional
    public Page<OperationWithWiringDTO> getAllByTemplate(String templateId, Pageable pageable) {
        Page<Operations> operationsPage = operationsRepository
                .findAllByTemplateIdOrderByLatestWiringDateDesc(templateId, pageable);

        return operationsPage.map(this::mapOpWithWires);
    }

    private OperationWithWiringDTO mapOpWithWires(Operations op) {
        List<SentWiring> wires = sentWiringRepository
                .findAllByOperations_IdOrderByCreatedDateAsc(op.getId());

        List<SentWiringDTO> wireDTOs = wires.stream()
                .map(SentWiringDTO::fromEntity)
                .toList();

        return OperationWithWiringDTO.of(op, wireDTOs);
    }

}

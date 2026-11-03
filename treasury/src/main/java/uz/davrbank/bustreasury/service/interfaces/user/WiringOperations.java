package uz.davrbank.bustreasury.service.interfaces.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import uz.davrbank.bustreasury.aggregate.dto.response.user.OperationWithWiringDTO;
import uz.davrbank.bustreasury.aggregate.model.user.SentWiring;

import java.util.List;

public interface WiringOperations {

    List<SentWiring> getAll();

    SentWiring getById(Long id);

    List<SentWiring> getByTransId(String transId);


    OperationWithWiringDTO getOneLatestPerWiring(Long operationId);

    Page<OperationWithWiringDTO> getAllWithWires(Pageable pageable);

    OperationWithWiringDTO getOneWithWiresByOperationId(Long operationId);

    Page<OperationWithWiringDTO> getAllByTemplate(String templateId, Pageable pageable);

}

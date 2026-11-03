package soap.humo_gateway.service.Impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import soap.humo_gateway.entity.RequestProtocol;
import soap.humo_gateway.repository.RequestRepository;
import soap.humo_gateway.service.RequestService;

@Service
@RequiredArgsConstructor
@Slf4j
public class RequestImpl implements RequestService {
    private final RequestRepository repository;

    @Override
    @Transactional
    public void createLog(RequestProtocol requestProtocol) {
        log.info("\nSaving request log:" +
                "\nparam: ................. {}" +
                "\nlogProtocolId: ......... {}" +
                "\nsubSystem: ............. {}" +
                "\ncreatedTime: ........... {}",
                "<<****secured****>>",
                requestProtocol.getLogProtocolId(), requestProtocol.getSubSystem(), requestProtocol.getCreatedTime());
        repository.save(requestProtocol);
    }
}

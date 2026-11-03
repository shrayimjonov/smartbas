package soap.humo_gateway.service.Impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import soap.humo_gateway.entity.Originator;
import soap.humo_gateway.repository.OriginatorRepository;
import soap.humo_gateway.service.OriginatorService;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class OriginatorServiceImpl implements OriginatorService {
    private final OriginatorRepository repository;

    @Override
    @Transactional(readOnly = true)
    public Optional<Originator> findByBankName(String bankName) {
        log.info("Find originator for bank: {}", bankName);
        return repository.findByBankName(bankName);
    }
}

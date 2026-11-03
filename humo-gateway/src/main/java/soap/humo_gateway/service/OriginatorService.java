package soap.humo_gateway.service;

import soap.humo_gateway.entity.Originator;

import java.util.Optional;

public interface OriginatorService {
    Optional<Originator> findByBankName(String bankName);
}

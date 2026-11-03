package uz.davrbank.partnervalidationservice.exception;

import java.util.UUID;

public class MerchantNotFoundException extends RuntimeException {

    public MerchantNotFoundException(UUID id) {
        super("Merchant not found: " + id);
    }

    public MerchantNotFoundException(String externalId) {
        super("Merchant not found for external id: " + externalId);
    }
}

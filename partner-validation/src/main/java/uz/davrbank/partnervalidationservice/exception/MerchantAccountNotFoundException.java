package uz.davrbank.partnervalidationservice.exception;

import java.util.UUID;

public class MerchantAccountNotFoundException extends RuntimeException {

    public MerchantAccountNotFoundException(String merchantId, String id) {
        super("Merchant account not found: merchantId=%s, accountId=%s".formatted(merchantId, id));
    }

    public MerchantAccountNotFoundException(UUID id) {
        super("Merchant account not found: accountId=%s".formatted(id));
    }
}

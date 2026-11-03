package uz.company.leskarserver.leskarintegration;

public record AvailableTypesItem(
        String sourceCurrency,
        String targetCurrency,
        String transferType,
        Limits limits
) {
}

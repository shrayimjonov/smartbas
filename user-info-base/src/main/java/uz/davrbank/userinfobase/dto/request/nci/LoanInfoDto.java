package uz.davrbank.userinfobase.dto.request.nci;

import uz.davrbank.userinfobase.entity.nci.LoanInfoEntity;

public record LoanInfoDto(
        String loanExtId,
        String mfo,
        String clientName,
        String clientBirthday,
        Long mainAccBalance,
        Long transitBalance,
        Long principalBalance,
        Long arrearsOnPrincipal,
        Long ofOverdueDebt,
        Long interestOnTermDebt,
        Long ofOverdueInterest,
        Long arrearsOfInterest,
        Long overdueCovenantCommission,
        Long recommendedAmount,
        Long maxAmount,
        Long loanAmount,
        String loanClose,
        String loanOpen,
        Long transitState,
        String transitAccount,
        String loanType,
        Long lossReserve,
        String currency,
        Long courtCosts
) {

    public static LoanInfoDto toDto(LoanInfoEntity entity) {
        return new LoanInfoDto(
                entity.getLoanExtId(),
                entity.getMfo(),
                entity.getClientName(),
                entity.getClientBirthday(),
                entity.getMainAccBalance(),
                entity.getTransitBalance(),
                entity.getPrincipalBalance(),
                entity.getArrearsOnPrincipal(),
                entity.getOfOverdueDebt(),
                entity.getInterestOnTermDebt(),
                entity.getOfOverdueInterest(),
                entity.getArrearsOfInterest(),
                entity.getOverdueCovenantCommission(),
                entity.getRecommendedAmount(),
                entity.getMaxAmount(),
                entity.getLoanAmount(),
                entity.getLoanClose(),
                entity.getLoanOpen(),
                entity.getTransitState(),
                entity.getTransitAccount(),
                entity.getLoanType(),
                entity.getLossReserve(),
                entity.getCurrency(),
                entity.getCourtCosts()
        );
    }
}
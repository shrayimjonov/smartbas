package uz.davrbank.partnervalidationservice.merchant.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.davrbank.partnervalidationservice.common.api.ResponsePageDTO;
import uz.davrbank.partnervalidationservice.common.query.PageResponseMapper;
import uz.davrbank.partnervalidationservice.exception.MerchantAccountNotFoundException;
import uz.davrbank.partnervalidationservice.merchant.dto.MerchantAccountDTO;
import uz.davrbank.partnervalidationservice.merchant.dto.MerchantAccountDetailDTO;
import uz.davrbank.partnervalidationservice.merchant.dto.MerchantAccountListDTO;
import uz.davrbank.partnervalidationservice.merchant.entity.MerchantAccount;
import uz.davrbank.partnervalidationservice.merchant.enums.AccountStatus;
import uz.davrbank.partnervalidationservice.merchant.filter.MerchantAccountFilter;
import uz.davrbank.partnervalidationservice.merchant.mapper.MerchantAccountMapper;
import uz.davrbank.partnervalidationservice.merchant.query.MerchantAccountSpecificationApplyFilterStrategy;
import uz.davrbank.partnervalidationservice.merchant.repository.MerchantAccountRepository;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MerchantAccountService {

    MerchantService merchantService;
    MerchantAccountRepository merchantAccountRepository;
    MerchantAccountSpecificationApplyFilterStrategy strategy;
    MerchantAccountMapper merchantAccountMapper;

    public MerchantAccountDetailDTO get(UUID id) {
        return merchantAccountMapper.toDetailDTO(findById(id));
    }

    public List<MerchantAccountListDTO> lookup(MerchantAccountFilter filter) {
        MerchantAccountFilter effectiveFilter = resolveFilter(filter);
        return merchantAccountRepository.findAll(strategy.applyFilter(MerchantAccount.class, effectiveFilter)).stream()
                .map(merchantAccountMapper::toListDTO)
                .toList();
    }

    public ResponsePageDTO<MerchantAccountListDTO> getPageable(MerchantAccountFilter filter) {
        MerchantAccountFilter effectiveFilter = resolveFilter(filter);
        Page<MerchantAccountListDTO> page = merchantAccountRepository.findAll(
                strategy.applyFilter(MerchantAccount.class, effectiveFilter),
                strategy.buildPage(effectiveFilter)
        ).map(merchantAccountMapper::toListDTO);
        return PageResponseMapper.fromPage(page);
    }

    @Transactional
    public UUID create(MerchantAccountDTO request) {
        merchantService.findEntityById(request.merchantId());
        ensureUniqueAccount(request.merchantId(), request, null);
        MerchantAccount account = merchantAccountMapper.toEntity(request);
        account.setMerchantId(request.merchantId());
        account.setStatus(AccountStatus.ACTIVE);
        return merchantAccountRepository.save(account).getId();
    }

    @Transactional
    public UUID update(UUID id, MerchantAccountDTO request) {
        MerchantAccount account = findById(id);
        UUID merchantId = account.getMerchantId();
        merchantService.findEntityById(merchantId);
        if (!Objects.equals(merchantId, request.merchantId())) {
            throw new IllegalArgumentException("Merchant account merchantId cannot be changed");
        }
        ensureUniqueAccount(merchantId, request, id);
        merchantAccountMapper.updateEntity(request, account);
        return merchantAccountRepository.save(account).getId();
    }

    @Transactional
    public void delete(UUID id) {
        MerchantAccount account = findById(id);
        account.setStatus(AccountStatus.BLOCKED);
        merchantAccountRepository.save(account);
    }

    public MerchantAccount findById(UUID id) {
        return merchantAccountRepository.findById(id)
                .orElseThrow(() -> new MerchantAccountNotFoundException(id));
    }

    private MerchantAccountFilter resolveFilter(MerchantAccountFilter filter) {
        return filter == null ? new MerchantAccountFilter() : filter;
    }

    private void ensureUniqueAccount(UUID merchantId, MerchantAccountDTO request, UUID id) {
        boolean exists = id == null
                ? merchantAccountRepository.existsByMerchantIdAndAccountNumberAndCurrency(
                merchantId,
                request.accountNumber(),
                request.currency()
        )
                : merchantAccountRepository.existsByMerchantIdAndAccountNumberAndCurrencyAndIdNot(
                merchantId,
                request.accountNumber(),
                request.currency(),
                id
        );
        if (exists) {
            throw new IllegalArgumentException("Merchant account already exists for merchant: " + request.accountNumber());
        }
    }
}

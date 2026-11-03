package uz.davrbank.partnervalidationservice.merchant.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.davrbank.partnervalidationservice.common.api.ResponsePageDTO;
import uz.davrbank.partnervalidationservice.common.query.PageResponseMapper;
import uz.davrbank.partnervalidationservice.exception.MerchantNotFoundException;
import uz.davrbank.partnervalidationservice.merchant.dto.MerchantDTO;
import uz.davrbank.partnervalidationservice.merchant.dto.MerchantDetailDTO;
import uz.davrbank.partnervalidationservice.merchant.dto.MerchantListDTO;
import uz.davrbank.partnervalidationservice.merchant.entity.Merchant;
import uz.davrbank.partnervalidationservice.merchant.enums.MerchantStatus;
import uz.davrbank.partnervalidationservice.merchant.filter.MerchantFilter;
import uz.davrbank.partnervalidationservice.merchant.mapper.MerchantMapper;
import uz.davrbank.partnervalidationservice.merchant.query.MerchantSpecificationApplyFilterStrategy;
import uz.davrbank.partnervalidationservice.merchant.repository.MerchantRepository;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MerchantService {

    MerchantRepository merchantRepository;
    MerchantSpecificationApplyFilterStrategy strategy;
    MerchantMapper merchantMapper;

    public MerchantDetailDTO get(UUID id) {
        return merchantMapper.toDetailDTO(findEntityById(id));
    }

    public List<MerchantListDTO> lookup(MerchantFilter filter) {
        MerchantFilter effectiveFilter = resolveFilter(filter);
        return merchantRepository.findAll(strategy.applyFilter(Merchant.class, effectiveFilter)).stream()
                .map(merchantMapper::toListDTO)
                .toList();
    }

    public ResponsePageDTO<MerchantListDTO> getPageable(MerchantFilter filter) {
        MerchantFilter effectiveFilter = resolveFilter(filter);
        Page<MerchantListDTO> page = merchantRepository.findAll(
                strategy.applyFilter(Merchant.class, effectiveFilter),
                strategy.buildPage(effectiveFilter)
        ).map(merchantMapper::toListDTO);
        return PageResponseMapper.fromPage(page);
    }

    @Transactional
    public UUID create(MerchantDTO request) {
        if (merchantRepository.existsByMerchantExternalId(request.merchantExternalId())) {
            throw new IllegalArgumentException("Merchant external id already exists: " + request.merchantExternalId());
        }

        Merchant merchant = merchantMapper.toEntity(request);
        merchant.setStatus(MerchantStatus.ACTIVE);
        merchant.setAllowedOperations(copyAllowedOperations(request.allowedOperations()));
        return merchantRepository.save(merchant).getId();
    }

    @Transactional
    public UUID update(UUID id, MerchantDTO request) {
        Merchant merchant = findEntityById(id);
        if (merchantRepository.existsByMerchantExternalIdAndIdNot(request.merchantExternalId(), id)) {
            throw new IllegalArgumentException("Merchant external id already exists: " + request.merchantExternalId());
        }
        merchantMapper.updateEntity(request, merchant);
        merchant.setAllowedOperations(copyAllowedOperations(request.allowedOperations()));
        return merchantRepository.save(merchant).getId();
    }

    @Transactional
    public void updateStatus(UUID id, MerchantStatus status) {
        Objects.requireNonNull(status, "Merchant status must not be null");
        Merchant merchant = findEntityById(id);
        merchant.setStatus(status);
        merchantRepository.save(merchant);
    }

    public Merchant findEntityById(UUID id) {
        return merchantRepository.findById(id)
                .orElseThrow(() -> new MerchantNotFoundException(id));
    }

    private MerchantFilter resolveFilter(MerchantFilter filter) {
        return filter == null ? new MerchantFilter() : filter;
    }

    private LinkedHashSet<String> copyAllowedOperations(LinkedHashSet<String> allowedOperations) {
        return allowedOperations == null ? new LinkedHashSet<>() : new LinkedHashSet<>(allowedOperations);
    }
}

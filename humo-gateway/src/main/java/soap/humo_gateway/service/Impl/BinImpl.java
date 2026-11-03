package soap.humo_gateway.service.Impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import soap.humo_gateway.entity.Bins;
import soap.humo_gateway.exception.CardTypeInCorrect;
import soap.humo_gateway.repository.BinsRepository;
import soap.humo_gateway.service.BinsService;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class BinImpl implements BinsService {
    private final BinsRepository repository;

    @Override
    @Cacheable(value = "bin")
    @Transactional(readOnly = true)
    public String findBankCodeByBin(String bin) {
        var bins = repository.findBinsByBinCode(bin);
        if (bins.isPresent())
            return bins.get().getBankCode();
        throw new CardTypeInCorrect("Bin with number: " + bin + " not found in database");
    }

    @Override
    @Cacheable(value = "centreId")
    @Transactional(readOnly = true)
    public Bins getCentreId(String pan) {
        return repository.findCentreIdByPan(pan)
                .orElseThrow(() -> new CardTypeInCorrect(String.format("Centre id for %s pan not found", pan)));

    }

    @Override
    @Cacheable(value = "binsList")
    @Transactional(readOnly = true)
    public List<Bins> findAllBanks() {
        log.info("Get All Bins...");
        return repository.findAll();
    }

    @Override
    @Cacheable(value = "davrBinsNotVisa")
    @Transactional(readOnly = true)
    public List<Bins> findDavrBankBinsNotVisa() {
        return repository.findDavrBinsNotVisa();
    }

    @Override
    @Cacheable(value = "davrBins")
    @Transactional(readOnly = true)
    public List<Bins> findDavrBankBins() {
        return repository.findDavrBins();
    }

    @Override
    @Cacheable(value = "visaBins")
    @Transactional(readOnly = true)
    public List<Bins> findCoBadgeBins() {
        return repository.findVisaBins();
    }

    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "bin", allEntries = true),
            @CacheEvict(value = "centreId", allEntries = true),
            @CacheEvict(value = "binsList",allEntries = true),
            @CacheEvict(value = "davrBinsNotVisa",allEntries = true),
            @CacheEvict(value = "davrBins",allEntries = true),
            @CacheEvict(value = "visaBins",allEntries = true)
    })
    public Bins save(Bins bins) {
        log.info("Saving bin: {}", bins);
        return repository.save(bins);
    }


}

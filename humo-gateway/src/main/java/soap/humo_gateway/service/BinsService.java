package soap.humo_gateway.service;

import soap.humo_gateway.entity.Bins;

import java.util.List;

public interface BinsService {
    String findBankCodeByBin(String bin);

    List<Bins> findAllBanks();

    List<Bins> findDavrBankBinsNotVisa();

    List<Bins> findCoBadgeBins();

    List<Bins> findDavrBankBins();

    Bins getCentreId(String pan);

    Bins save(Bins bins);
}

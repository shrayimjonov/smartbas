package davr.uzcard.Service.Feign;

import davr.uzcard.Exception.BadRequestException;
import davr.uzcard.Model.response.UzcardResponse;
import davr.uzcard.Service.FeignImpl;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class FeignService implements FeignImpl {
    private final SvGateFeign svGateFeign;
    private final SvGateHistoryFeign historyFeign;
    private final SvGateTransactionFeign transactionFeign;
    private final GenesisFeign genesisFeign;
    private final BoGateFeign boGateFeign;
    private final SvGateEightFeign svGateEightFeign;

    @Override
    public <E> Object svgateConnect(E value) {
        try {
            return svGateFeign.svgateConnect(value);
        } catch (Exception ex) {
            log.error("svgate exception: ", ex);
            throw new BadRequestException(ex.getMessage());
        }
    }


    @Override
    public <E> Object svgateHistory(E value) {
        return historyFeign.svgateConnect(value);
    }

    @Override
    public <E> Object svgateBinList(E value) {
        return svGateFeign.svgateConnect(value);
    }


    @Override
    public <E> Object svgateTransaction(E value) {
        try {
            return transactionFeign.svgateConnect(value);
        } catch (FeignException f) {
            return new UzcardResponse<>(
                    System.currentTimeMillis(),
                    "2.0",
                    new UzcardResponse.Error(505L, f.getMessage())
            );
        }
    }

    @Override
    public <E> Object genesis(E value) {
        return genesisFeign.connect(value);
    }

    @Override
    public <E> Object boGateConnect(E value) {
        return boGateFeign.boGateConnect(value);
    }


    @Override
    public <E> Object svgateEightConnect(E value) {
        try {
            return svGateEightFeign.connect(value);
        } catch (Exception ex) {
            log.error("svgate eight exception: ", ex);
            throw new BadRequestException(ex.getMessage());
        }
    }

}

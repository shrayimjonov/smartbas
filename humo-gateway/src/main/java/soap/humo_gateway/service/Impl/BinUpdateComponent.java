package soap.humo_gateway.service.Impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import soap.humo_gateway.dto.property.HumoIntegrationProperty;
import soap.humo_gateway.dto.response.humoIntegration.BinResponse;
import soap.humo_gateway.entity.Bins;
import soap.humo_gateway.entity.Originator;
import soap.humo_gateway.service.BinsService;
import soap.humo_gateway.service.HumoIntegrationClientService;
import soap.humo_gateway.service.OriginatorService;

import java.util.List;
import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor
@Profile("adm")
public class BinUpdateComponent {
    private final HumoIntegrationProperty humoIntegrationProperty;
    private final HumoIntegrationClientService clientService;
    private final OriginatorService originatorService;
    private final BinsService binsService;

    @Scheduled(cron = "0 0 0 ? * * *")
    public void getBins() {
        log.info("Start find new bins updater...");
        BinResponse binsResponse = clientService.sendGetRequest(BinResponse.class, humoIntegrationProperty.getUrl() + "/bins");

        binsResponse
                .getResult()
                .getData()
                .forEach(z -> {
                    log.info("Match bin: {} to existing", z);

                    List<Bins> bins = binsService.findAllBanks();

                    if (bins
                            .stream()
                            .noneMatch(b -> z.getBin_code().equals(b.getBinCode()))) {
                        log.info("Bin: {} not found and will be added", z.getBin_code());

                        Optional<Originator> originatorOptional = originatorService.findByBankName(z.getBank_name());

                        if (!originatorOptional.isPresent()) {
                            log.error("Originator: {} not found in database...", z.getBank_name());
                            return;
                        }

                        Bins preparedBin = new Bins(
                                z.getBank_c(),
                                z.getBank_name(),
                                z.getBank_name() + " " + z.getBin_owner(),
                                z.getBin_code(),
                                z.getBin_owner().equals("PERSONAL"),
                                originatorOptional.get().getOriginator()
                        );

                        binsService.save(preparedBin);
                    }

                });

        log.info("Bins updater finished!");
    }
}

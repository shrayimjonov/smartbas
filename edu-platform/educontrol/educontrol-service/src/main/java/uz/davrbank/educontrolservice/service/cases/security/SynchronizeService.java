package uz.davrbank.educontrolservice.service.cases.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uz.davrbank.educontrolservice.aggregate.dto.security.sync.Sync;
import uz.davrbank.educontrolservice.feign.security.SynchronizeInterfaceFeign;
import uz.davrbank.educontrolservice.service.dao.security.SynchronizeInterface;

import java.util.List;

@Service
@Slf4j
public class SynchronizeService implements SynchronizeInterface {

    private final SynchronizeInterfaceFeign synchronizeInterfaceFeign;

    public SynchronizeService(SynchronizeInterfaceFeign synchronizeInterfaceFeign) {
        this.synchronizeInterfaceFeign = synchronizeInterfaceFeign;
    }

    @Override
    public List<Object> getProviders() {
        log.info("REQUEST (getProviders) FROM [security-service]");
        return synchronizeInterfaceFeign.getComponents();
    }

    @Override
    public Object userSync(String providerId, Sync action) {
        log.info("REQUEST (userSync) FROM [security-service]");
        return synchronizeInterfaceFeign.getSynchronize(providerId, action);
    }
}

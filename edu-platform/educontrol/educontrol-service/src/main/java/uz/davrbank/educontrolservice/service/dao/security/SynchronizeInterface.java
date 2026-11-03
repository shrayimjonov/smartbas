package uz.davrbank.educontrolservice.service.dao.security;

import uz.davrbank.educontrolservice.aggregate.dto.security.sync.Sync;

import java.util.List;

public interface SynchronizeInterface {

    List<Object> getProviders();

    Object userSync(String providerId, Sync action);
}

package uz.davrbank.userinfobase.service;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DbService {

    private final EntityManager entityManager;

    @Transactional
    public void dropDavrhubTable() {
        entityManager.createNativeQuery("DROP TABLE IF EXISTS davrhub_files CASCADE")
                .executeUpdate();
    }

}
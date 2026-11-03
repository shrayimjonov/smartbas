package uz.davrbank.userinfobase.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.davrbank.userinfobase.entity.AuthTokenDavrHub;

@Repository
public interface AuthTokenDavrHubRepository extends JpaRepository<AuthTokenDavrHub, Long> {
}
package uz.davrbank.userinfobase.repository.nci;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import uz.davrbank.userinfobase.entity.nci.PersonDetails;

import java.util.Optional;


@Repository
public interface PersonalDetailsRepository extends JpaRepository<PersonDetails, Long>, JpaSpecificationExecutor<PersonDetails> {
    Optional<PersonDetails> findByPinfl(String pinfl);

}

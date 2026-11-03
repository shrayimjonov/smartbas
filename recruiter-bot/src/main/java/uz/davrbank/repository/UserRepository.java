package uz.davrbank.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.davrbank.entity.Department;
import uz.davrbank.entity.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Page<User> findAllByOrderByIdDesc(Pageable pageable);
    User findTopByPhoneAndDepartmentOrderByCreatedAtDesc(String phone, Department department);

    Optional<User> findByUsername(String username);
}

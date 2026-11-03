package uz.davrbank.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uz.davrbank.entity.CreateTest;
import uz.davrbank.entity.Department;

import java.util.List;

@Repository
public interface CreateTestRepository extends JpaRepository<CreateTest, Long>, JpaSpecificationExecutor<CreateTest> {

    boolean existsByDepartmentId(Long department_id);
}
package uz.davrbank.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.davrbank.entity.PhotoUpload;

import java.util.Optional;

@Repository
public interface PhotoUploadRepository extends JpaRepository<PhotoUpload, Long> {

}

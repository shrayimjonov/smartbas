package uz.davrbank.assignmentservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.davrbank.assignmentservice.aggregation.model.task.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

//    List<Task> findAllByStatus(Task status);
}

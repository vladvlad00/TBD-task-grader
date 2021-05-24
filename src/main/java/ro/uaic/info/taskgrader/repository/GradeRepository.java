package ro.uaic.info.taskgrader.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ro.uaic.info.taskgrader.entity.Grade;
import ro.uaic.info.taskgrader.entity.GradePK;

public interface GradeRepository extends CrudRepository<Grade, GradePK>
{
    @Query(value = "select * from grade where task_id=?1", nativeQuery = true)
    Iterable<Grade> findByTaskId(Integer taskId);

    @Query(value = "select * from grade where student_id=?1", nativeQuery = true)
    Iterable<Grade> findByStudentId(Integer studentId);
}

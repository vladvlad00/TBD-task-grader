package ro.uaic.info.taskgrader.repository;

import org.springframework.data.repository.CrudRepository;
import ro.uaic.info.taskgrader.entity.SheetStudent;
import ro.uaic.info.taskgrader.entity.SheetStudentPK;

public interface SheetStudentRepository extends CrudRepository<SheetStudent, SheetStudentPK> {
    public Iterable<SheetStudent> findAllByIdSheetId(Integer id);

    public Iterable<SheetStudent> findAllByIdStudentId(Integer id);
}

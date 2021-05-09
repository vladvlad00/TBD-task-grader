package ro.uaic.info.taskgrader.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ro.uaic.info.taskgrader.entity.SheetTaskPK;
import ro.uaic.info.taskgrader.entity.SheetTask;

import java.util.Optional;

public interface SheetTaskRepository extends CrudRepository<SheetTask, SheetTaskPK> {
        public Iterable<SheetTask> findAllByIdSheetId(Integer id);
}

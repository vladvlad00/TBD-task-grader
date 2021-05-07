package ro.uaic.info.taskgrader.repository;

import org.springframework.data.repository.CrudRepository;
import ro.uaic.info.taskgrader.entity.Sheet;

public interface SheetRepository extends CrudRepository<Sheet, Integer> {
}

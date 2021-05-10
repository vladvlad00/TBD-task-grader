package ro.uaic.info.taskgrader.entity;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Entity
public class SheetStudent {
    @EmbeddedId
    private SheetStudentPK id;

    public SheetStudent() {
    }

    public SheetStudent(SheetStudentPK id) {
        this.id = id;
    }

    public SheetStudentPK getId() {
        return id;
    }

    public void setId(SheetStudentPK id) {
        this.id = id;
    }
}

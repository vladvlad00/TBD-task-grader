package ro.uaic.info.taskgrader.entity;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Entity
public class SheetTask
{
    @EmbeddedId
    private SheetTaskPK id;

    public SheetTask()
    {
    }

    public SheetTask(SheetTaskPK id)
    {
        this.id = id;
    }

    public SheetTaskPK getId()
    {
        return id;
    }

    public void setId(SheetTaskPK id)
    {
        this.id = id;
    }
}

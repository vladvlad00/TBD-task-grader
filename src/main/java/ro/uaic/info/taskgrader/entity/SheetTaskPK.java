package ro.uaic.info.taskgrader.entity;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class SheetTaskPK implements Serializable
{
    private Integer sheetId;
    private Integer taskId;

    public SheetTaskPK()
    {
    }

    public SheetTaskPK(Integer sheetId, Integer taskId)
    {
        this.sheetId = sheetId;
        this.taskId = taskId;
    }

    public Integer getSheetId()
    {
        return sheetId;
    }

    public void setSheetId(Integer sheetId)
    {
        this.sheetId = sheetId;
    }

    public Integer getTaskId()
    {
        return taskId;
    }

    public void setTaskId(Integer taskId)
    {
        this.taskId = taskId;
    }
}

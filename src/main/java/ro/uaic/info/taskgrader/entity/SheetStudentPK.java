package ro.uaic.info.taskgrader.entity;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class SheetStudentPK implements Serializable
{
    private Integer sheetId;
    private Integer studentId;

    public SheetStudentPK()
    {
    }

    public SheetStudentPK(Integer sheetId, Integer studentId)
    {
        this.sheetId = sheetId;
        this.studentId = studentId;
    }

    public Integer getSheetId()
    {
        return sheetId;
    }

    public void setSheetId(Integer sheetId)
    {
        this.sheetId = sheetId;
    }

    public Integer getStudentId()
    {
        return studentId;
    }

    public void setStudentId(Integer studentId)
    {
        this.studentId = studentId;
    }
}

package ro.uaic.info.taskgrader.entity;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class GradePK implements Serializable
{
    private Integer studentId;
    private Integer taskId;

    public GradePK()
    {
    }

    public GradePK(Integer taskId, Integer studentId)
    {
        this.studentId = studentId;
        this.taskId = taskId;
    }

    public Integer getStudentId()
    {
        return studentId;
    }

    public void setStudentId(Integer studentId)
    {
        this.studentId = studentId;
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

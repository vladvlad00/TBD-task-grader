package ro.uaic.info.taskgrader.entity;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Entity
public class Grade
{
    @EmbeddedId
    private GradePK id;

    private Integer grade;

    public GradePK getId()
    {
        return id;
    }

    public void setId(GradePK id)
    {
        this.id = id;
    }

    public Integer getGrade()
    {
        return grade;
    }

    public void setGrade(Integer grade)
    {
        this.grade = grade;
    }
}

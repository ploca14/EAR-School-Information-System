package cz.cvut.kbss.ear.project.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.util.List;

@Entity
@DiscriminatorValue("TEACHER")
public class CourseTeacher extends CourseParticipant{

    @ManyToMany
    private List<Parallel> teachedParallels;

    public List<Parallel> getTeachedParallels() {
        return teachedParallels;
    }

    public void setTeachedParallels(List<Parallel> teachedParallels) {
        this.teachedParallels = teachedParallels;
    }
}

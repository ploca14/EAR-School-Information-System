package cz.cvut.kbss.ear.project.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.util.List;

@Entity
@DiscriminatorValue("STUDENT")
public class CourseStudent extends CourseParticipant {

    @ManyToMany
    private List<Parallel> parallels;

    public List<Parallel> getParallels() {
        return parallels;
    }

    public void setParallels(List<Parallel> parallels) {
        this.parallels = parallels;
    }

    @Override
    public String toString() {
        return "CourseStudent{" +
                "enroledParallels=" + parallels +
                '}';
    }
}

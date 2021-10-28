package cz.cvut.kbss.ear.project.model;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.util.List;

@Entity
@DiscriminatorValue("STUDENT")
public class CourseStudent extends CourseParticipant{

    @ManyToMany
    private List<Parallel> enroledParallels;

    public List<Parallel> getEnroledParallels() {
        return enroledParallels;
    }

    public void setEnroledParallels(List<Parallel> enroledParallels) {
        this.enroledParallels = enroledParallels;
    }
}

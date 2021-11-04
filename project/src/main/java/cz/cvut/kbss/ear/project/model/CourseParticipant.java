package cz.cvut.kbss.ear.project.model;

import java.util.Collection;
import java.util.HashSet;
import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "PARTICIPANT_TYPE")
@NamedQueries({
    @NamedQuery(name = "CourseStudent.findByUser", query = "SELECT cs from CourseStudent cs WHERE :user = cs.user"),
    @NamedQuery(name = "CourseTeacher.findByUser", query = "SELECT ct from CourseTeacher ct WHERE :user = ct.user"),
    @NamedQuery(name = "CourseParticipant.findByUser", query = "SELECT cp from CourseParticipant cp WHERE :user = cp.user"),
    @NamedQuery(name = "CourseParticipant.findByUserAndCourse", query = "SELECT cp from CourseParticipant cp WHERE :user = cp.user AND :course = cp.course")
})
public abstract class CourseParticipant extends AbstractEntity {

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(optional = false, cascade = CascadeType.PERSIST)
    private CourseInSemester course;

    @ManyToMany
    private Collection<Parallel> parallels = new HashSet<>();

    public Collection<Parallel> getParallels() {
        return parallels;
    }

    public void setParallels(Collection<Parallel> parallels) {
        this.parallels = parallels;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public CourseInSemester getCourse() {
        return course;
    }

    public void setCourse(CourseInSemester course) {
        this.course = course;
    }

    @Override
    public String toString() {
        return "CourseParticipant{" +
            "user=" + user +
            '}';
    }

    public abstract void enrollInParallel(Parallel parallel);

    public abstract void unenrollFromParallel(Parallel parallel);

    public abstract void unenrollFromCourse();
}

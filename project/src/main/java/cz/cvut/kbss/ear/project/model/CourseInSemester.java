package cz.cvut.kbss.ear.project.model;

import com.sun.istack.NotNull;
import java.util.Collection;
import java.util.HashSet;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

@Entity
@NamedQueries({
    @NamedQuery(
        name = "CourseInSemester.findCourseInSemester",
        query = "SELECT c FROM CourseInSemester c WHERE c.semester = :semester AND c.course = :course"
    )
})
public class CourseInSemester extends AbstractEntity {

    @ManyToOne(optional = false)
    @NotNull
    private Course course;

    @ManyToOne(optional = false)
    @NotNull
    private Semester semester;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private Collection<CourseTeacher> teachers = new HashSet<>();

    @OneToMany(mappedBy = "course", cascade = CascadeType.MERGE)
    private Collection<Parallel> parallels = new HashSet<>();

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Semester getSemester() {
        return semester;
    }

    public void setSemester(Semester semester) {
        this.semester = semester;
    }

    public Collection<CourseTeacher> getTeachers() {
        return teachers;
    }

    public void setTeachers(Collection<CourseTeacher> teachers) {
        this.teachers = teachers;
    }

    public Collection<Parallel> getParallels() {
        return parallels;
    }

    public void setParallels(Collection<Parallel> parallels) {
        this.parallels = parallels;
    }

    public void addTeacher(CourseTeacher teacher) {
        teachers.add(teacher);
        teacher.setCourse(this);
    }

    public void addParallel(Parallel parallel) {
        parallels.add(parallel);
        parallel.setCourseInSemester(this);
    }

    public void removeParallel(Parallel parallel) {
        parallels.remove(parallel);
        parallel.setCourseInSemester(null);
    }
}

package cz.cvut.kbss.ear.project.model;

import com.sun.istack.NotNull;
import java.util.Collection;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class CourseInSemester extends AbstractEntity {

  @ManyToOne(optional = false)
  @NotNull
  private Course course;

  @ManyToOne(optional = false)
  @NotNull
  private Semester semester;

  @OneToMany(mappedBy = "course")
  private Collection<CourseTeacher> teachers;

  @OneToMany(mappedBy = "course")
  private Collection<Parallel> parallels;

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
}

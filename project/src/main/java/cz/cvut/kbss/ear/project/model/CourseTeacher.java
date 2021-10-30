package cz.cvut.kbss.ear.project.model;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import java.util.List;

@Entity
public class CourseTeacher extends CourseParticipant {

  @ManyToOne(optional = false)
  private CourseInSemester course;

  @ManyToMany
  private List<Parallel> parallels;

  public CourseInSemester getCourse() {
    return course;
  }

  public void setCourse(CourseInSemester course) {
    this.course = course;
  }
}

package cz.cvut.kbss.ear.project.model;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class CourseTeacher extends AbstractEntity {

  @ManyToOne(optional = false)
  private CourseInSemester course;

  public CourseInSemester getCourse() {
    return course;
  }

  public void setCourse(CourseInSemester course) {
    this.course = course;
  }
}

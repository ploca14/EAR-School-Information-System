package cz.cvut.kbss.ear.project.model;

import com.sun.istack.NotNull;
import cz.cvut.kbss.ear.project.model.enums.CourseCompletionType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Entity
public class Course extends AbstractEntity {

  @NotNull
  private String name;

  @NotNull
  private Integer credits;

  private String description;

  @NotNull
  private String code;

  private Integer recommendedSemester;

  @Enumerated(EnumType.STRING)
  private CourseCompletionType completionType;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Integer getCredits() {
    return credits;
  }

  public void setCredits(Integer credits) {
    this.credits = credits;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public Integer getRecommendedSemester() {
    return recommendedSemester;
  }

  public void setRecommendedSemester(Integer recommendedSemester) {
    this.recommendedSemester = recommendedSemester;
  }

  public CourseCompletionType getCompletionType() {
    return completionType;
  }

  public void setCompletionType(CourseCompletionType completionType) {
    this.completionType = completionType;
  }

  @Override
  public String toString() {
    return "Course{" +
        "name='" + name + '\'' +
        ", credits=" + credits +
        ", description='" + description + '\'' +
        ", code='" + code + '\'' +
        ", recommendedSemester=" + recommendedSemester +
        ", completionType=" + completionType +
        '}';
  }
}

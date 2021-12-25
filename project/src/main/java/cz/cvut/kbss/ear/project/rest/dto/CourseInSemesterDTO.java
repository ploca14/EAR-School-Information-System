package cz.cvut.kbss.ear.project.rest.dto;

import cz.cvut.kbss.ear.project.model.Course;
import cz.cvut.kbss.ear.project.model.CourseInSemester;
import cz.cvut.kbss.ear.project.model.CourseTeacher;
import cz.cvut.kbss.ear.project.model.Semester;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class CourseInSemesterDTO {
    private Course course;

    private Semester semester;

    private List<String> teachers;

    public CourseInSemesterDTO() {
    }

    public CourseInSemesterDTO(CourseInSemester courseInSemester) {
        course = courseInSemester.getCourse();
        semester = courseInSemester.getSemester();
        for (CourseTeacher u : courseInSemester.getTeachers()){
            System.out.println(u);
        }
        teachers = courseInSemester.getTeachers().stream().map(e -> (e.getUser().getUsername())).collect(Collectors.toList());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CourseInSemesterDTO that = (CourseInSemesterDTO) o;
        return Objects.equals(course, that.course) && Objects.equals(semester, that.semester) && Objects.equals(teachers, that.teachers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(course, semester, teachers);
    }

    @Override
    public String toString() {
        return "CourseInSemesterDTO{" +
                "course=" + course +
                ", semester=" + semester +
                ", teachers=" + teachers +
                '}';
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public void setSemester(Semester semester) {
        this.semester = semester;
    }

    public void setTeachers(List<String> teachers) {
        this.teachers = teachers;
    }

    public Course getCourse() {
        return course;
    }

    public Semester getSemester() {
        return semester;
    }

    public List<String> getTeachers() {
        return teachers;
    }
}

package cz.cvut.kbss.ear.project.model;

import com.sun.istack.NotNull;
import cz.cvut.kbss.ear.project.model.enums.ParallelType;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Entity
public class Parallel extends AbstractEntity{

    @NotNull
    private String name;

    @NotNull
    private Timestamp startTime;

    @NotNull
    private Timestamp endTime;

    private String note;

    @NotNull
    private Integer capacity;

    @Enumerated(EnumType.STRING)
    private ParallelType parallelType;

    @ManyToOne
    @JoinColumn(name = "course_in_semester_id")
    private CourseInSemester course;

    @ManyToMany(mappedBy = "enroledParallels")
    private List<CourseStudent> courseStudents;

    @ManyToMany(mappedBy = "teachedParallels")
    private List<CourseTeacher> courseTeachers;

    @ManyToOne
    @JoinColumn(name = "classroom_id")
    private Classroom classroom;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public ParallelType getParallelType() {
        return parallelType;
    }

    public void setParallelType(ParallelType parallelType) {
        this.parallelType = parallelType;
    }

    public CourseInSemester getCourseInSemester() {
        return courseInSemester;
    }

    public void setCourseInSemester(CourseInSemester courseInSemester) {
        this.courseInSemester = courseInSemester;
    }

    public List<CourseStudent> getCourseStudents() {
        return courseStudents;
    }

    public void setCourseStudents(List<CourseStudent> courseStudents) {
        this.courseStudents = courseStudents;
    }

    public List<CourseTeacher> getCourseTeachers() {
        return courseTeachers;
    }

    public void setCourseTeachers(List<CourseTeacher> courseTeachers) {
        this.courseTeachers = courseTeachers;
    }

    public Classroom getClassroom() {
        return classroom;
    }

    public void setClassroom(Classroom classroom) {
        this.classroom = classroom;
    }

    @Override
    public String toString() {
        return "Parallel{" +
                "name='" + name + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", note='" + note + '\'' +
                ", capacity=" + capacity +
                ", parallelType=" + parallelType +
                ", courseInSemester=" + courseInSemester +
                ", courseStudents=" + courseStudents +
                ", courseTeachers=" + courseTeachers +
                ", classroom=" + classroom +
                '}';
    }
}

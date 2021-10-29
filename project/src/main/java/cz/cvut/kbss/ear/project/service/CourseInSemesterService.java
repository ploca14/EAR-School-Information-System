package cz.cvut.kbss.ear.project.service;

import cz.cvut.kbss.ear.project.dao.CourseStudentDao;
import cz.cvut.kbss.ear.project.dao.CourseTeacherDao;
import cz.cvut.kbss.ear.project.model.CourseInSemester;
import cz.cvut.kbss.ear.project.model.User;
import org.springframework.stereotype.Service;

@Service
public class CourseInSemesterService {
    private final CourseTeacherDao courseTeacherDao;

    private final CourseStudentDao courseStudentDao;

    public CourseInSemesterService(CourseTeacherDao courseTeacherDao, CourseStudentDao courseStudentDao) {
        this.courseTeacherDao = courseTeacherDao;
        this.courseStudentDao = courseStudentDao;
    }

    public void createNewCourseInSemester(){

    }

    public void enrolStudentInCourse(User user, CourseInSemester course){

    }

    public void enrolTeacherInCourse(User user, CourseInSemester course){
    }

    public void unenrolFromCourse(User user, CourseInSemester course){

    }

    public boolean isUserEnroled(User user, CourseInSemester course){
        return false;
    }

    public boolean isUserTeacher(User user, CourseInSemester course){
        return false;
    }

    public boolean isUserStudent(User user, CourseInSemester course){
        return false;
    }
}

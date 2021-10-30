package cz.cvut.kbss.ear.project.service;

import cz.cvut.kbss.ear.project.dao.CourseInSemesterDao;
import cz.cvut.kbss.ear.project.dao.CourseStudentDao;
import cz.cvut.kbss.ear.project.dao.CourseTeacherDao;
import cz.cvut.kbss.ear.project.exception.CourseException;
import cz.cvut.kbss.ear.project.model.Course;
import cz.cvut.kbss.ear.project.model.CourseInSemester;
import cz.cvut.kbss.ear.project.model.Semester;
import cz.cvut.kbss.ear.project.model.User;
import org.springframework.stereotype.Service;

@Service
public class CourseInSemesterService {
    private final CourseTeacherDao courseTeacherDao;

    private final CourseStudentDao courseStudentDao;

    private final CourseInSemesterDao courseInSemesterDao;

    public CourseInSemesterService(CourseTeacherDao courseTeacherDao, CourseStudentDao courseStudentDao, CourseInSemesterDao courseInSemesterDao) {
        this.courseTeacherDao = courseTeacherDao;
        this.courseStudentDao = courseStudentDao;
        this.courseInSemesterDao = courseInSemesterDao;
    }

    public CourseInSemester createNewCourseInSemester(Course course, Semester semester){
        if (courseInstanceExists(course, semester)){
            throw new CourseException("Course: " + course.getCode() +
                    " already has an instance in semester " + semester.getCode());
        }

        CourseInSemester courseInSemester = new CourseInSemester();
        courseInSemester.setCourse(course);
        courseInSemester.setSemester(semester);
        courseInSemesterDao.persist(courseInSemester);

        return courseInSemester;
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

    public boolean courseInstanceExists(Course course, Semester semester){
        return courseInSemesterDao.findCourseInSemester(course, semester) != null;
    }
}

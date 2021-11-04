package cz.cvut.kbss.ear.project.service;

import cz.cvut.kbss.ear.project.dao.CourseInSemesterDao;
import cz.cvut.kbss.ear.project.dao.CourseParticipantDao;
import cz.cvut.kbss.ear.project.dao.CourseStudentDao;
import cz.cvut.kbss.ear.project.dao.CourseTeacherDao;
import cz.cvut.kbss.ear.project.exception.CourseException;
import cz.cvut.kbss.ear.project.model.Course;
import cz.cvut.kbss.ear.project.model.CourseInSemester;
import cz.cvut.kbss.ear.project.model.CourseParticipant;
import cz.cvut.kbss.ear.project.model.CourseStudent;
import cz.cvut.kbss.ear.project.model.CourseTeacher;
import cz.cvut.kbss.ear.project.model.Semester;
import cz.cvut.kbss.ear.project.model.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CourseInSemesterService {

    private final CourseTeacherDao courseTeacherDao;

    private final CourseStudentDao courseStudentDao;

    private final CourseParticipantDao courseParticipantDao;

    private final CourseInSemesterDao courseInSemesterDao;

    public CourseInSemesterService(CourseTeacherDao courseTeacherDao,
        CourseStudentDao courseStudentDao,
        CourseParticipantDao courseParticipantDao,
        CourseInSemesterDao courseInSemesterDao) {
        this.courseTeacherDao = courseTeacherDao;
        this.courseStudentDao = courseStudentDao;
        this.courseParticipantDao = courseParticipantDao;
        this.courseInSemesterDao = courseInSemesterDao;
    }

    public CourseInSemester createNewCourseInSemester(Course course, Semester semester) {
        if (courseInstanceExists(course, semester)) {
            throw new CourseException("Course: " + course.getCode() +
                " already has an instance in semester " + semester.getCode());
        }

        CourseInSemester courseInSemester = new CourseInSemester();
        courseInSemester.setCourse(course);
        courseInSemester.setSemester(semester);
        courseInSemesterDao.persist(courseInSemester);

        return courseInSemester;
    }

    @Transactional
    public CourseStudent enrolAsStudentInCourse(User user, CourseInSemester course) {
        CourseStudent student = new CourseStudent();
        student.setCourse(course);
        student.setUser(user);
        courseStudentDao.persist(student);
        return student;
    }

    @Transactional
    public CourseTeacher enrolAsTeacherInCourse(User user, CourseInSemester course) {
        CourseTeacher teacher = new CourseTeacher();
        teacher.setUser(user);
        course.addTeacher(teacher);
        courseTeacherDao.persist(teacher);
        return teacher;
    }

    public void unenrolFromCourse(User user, CourseInSemester course) {
        CourseParticipant participant = courseParticipantDao.findByUserAndCourse(user, course);
        participant.unenrollFromCourse();
        courseParticipantDao.remove(participant);
    }

    public boolean isUserEnroled(User user, CourseInSemester course) {
        CourseParticipant participant = courseParticipantDao.findByUserAndCourse(user, course);
        return participant != null;
    }

    public boolean courseInstanceExists(Course course, Semester semester) {
        return courseInSemesterDao.findCourseInSemester(course, semester) != null;
    }
}

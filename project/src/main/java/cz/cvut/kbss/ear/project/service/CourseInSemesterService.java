package cz.cvut.kbss.ear.project.service;

import cz.cvut.kbss.ear.project.dao.*;
import cz.cvut.kbss.ear.project.exception.CourseException;
import cz.cvut.kbss.ear.project.model.Course;
import cz.cvut.kbss.ear.project.model.CourseInSemester;
import cz.cvut.kbss.ear.project.model.CourseParticipant;
import cz.cvut.kbss.ear.project.model.CourseStudent;
import cz.cvut.kbss.ear.project.model.CourseTeacher;
import cz.cvut.kbss.ear.project.model.Parallel;
import cz.cvut.kbss.ear.project.model.Semester;
import cz.cvut.kbss.ear.project.model.User;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CourseInSemesterService {

    private final CourseTeacherDao courseTeacherDao;

    private final CourseStudentDao courseStudentDao;

    private final CourseParticipantDao courseParticipantDao;

    private final CourseDao courseDao;

    private final SemesterDao semesterDao;

    private final CourseInSemesterDao courseInSemesterDao;

    private final KosapiService kosapiService;

    public CourseInSemesterService(
        CourseTeacherDao courseTeacherDao,
        CourseStudentDao courseStudentDao,
        CourseParticipantDao courseParticipantDao,
        CourseInSemesterDao courseInSemesterDao,
        CourseDao courseDao,
        SemesterDao semesterDao,
        KosapiService kosapiService
    ) {
        this.courseTeacherDao = courseTeacherDao;
        this.courseStudentDao = courseStudentDao;
        this.courseParticipantDao = courseParticipantDao;
        this.courseDao = courseDao;
        this.semesterDao = semesterDao;
        this.courseInSemesterDao = courseInSemesterDao;
        this.kosapiService = kosapiService;
    }

    @Transactional
    public List<CourseInSemester> findAll() {
        return courseInSemesterDao.findAll();
    }

    @Transactional
    public CourseInSemester find(Integer id) {
        return courseInSemesterDao.find(id);
    }

    @Transactional
    public CourseInSemester findByCode(String course_code, String semester_code){
        return courseInSemesterDao.findCourseInSemester(courseDao.findByCode(course_code), semesterDao.findByCode(semester_code));
    }

    @Transactional
    public void persist(CourseInSemester courseInSemester) {
        courseInSemesterDao.persist(courseInSemester);
    }

    @Transactional
    public CourseInSemester addCourseToSemester(Course course, Semester semester) {
        Objects.requireNonNull(course);
        Objects.requireNonNull(semester);

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
        Objects.requireNonNull(user);
        Objects.requireNonNull(course);

        CourseStudent student = new CourseStudent();
        student.setCourse(course);
        student.setUser(user);
        courseStudentDao.persist(student);
        return student;
    }

    @Transactional
    public CourseTeacher enrolAsTeacherInCourse(User user, CourseInSemester course) {
        Objects.requireNonNull(user);
        Objects.requireNonNull(course);

        CourseTeacher teacher = new CourseTeacher();
        teacher.setUser(user);
        course.addTeacher(teacher);
        courseTeacherDao.persist(teacher);
        return teacher;
    }

    @Transactional
    public void unenrolFromCourse(User user, CourseInSemester course) {
        Objects.requireNonNull(user);
        Objects.requireNonNull(course);

        CourseParticipant participant = courseParticipantDao.findByUserAndCourse(user, course);
        participant.unenrollFromCourse();
        courseParticipantDao.remove(participant);
    }

    @Transactional
    public boolean isUserEnroled(User user, CourseInSemester course) {
        Objects.requireNonNull(user);
        Objects.requireNonNull(course);

        CourseParticipant participant = courseParticipantDao.findByUserAndCourse(user, course);
        return participant != null;
    }

    @Transactional
    public boolean courseInstanceExists(Course course, Semester semester) {
        Objects.requireNonNull(course);
        Objects.requireNonNull(semester);

        return courseInSemesterDao.findCourseInSemester(course, semester) != null;
    }

    @Transactional
    public Collection<Parallel> getParallels(CourseInSemester courseInSemester) {
        Objects.requireNonNull(courseInSemester);

        return courseInSemesterDao.find(courseInSemester.getId()).getParallels();
    }

    @Transactional
    public List<CourseStudent> getStudents(CourseInSemester courseInSemester){
        Objects.requireNonNull(courseInSemester);

        return courseInSemesterDao.findStudents(courseInSemester);
    }

    @Transactional
    public List<CourseTeacher> getTeachers(CourseInSemester courseInSemester){
        Objects.requireNonNull(courseInSemester);

        return courseInSemesterDao.findTeachers(courseInSemester);
    }

    @Transactional
    public List<CourseParticipant> getAllParticipants(CourseInSemester courseInSemester){
        Objects.requireNonNull(courseInSemester);

        return courseInSemesterDao.findAllParticipants(courseInSemester);
    }

    @Transactional
    public CourseParticipant getCourseParticipant(CourseInSemester courseInSemester, User user){
        Objects.requireNonNull(courseInSemester);
        Objects.requireNonNull(user);

        return courseInSemesterDao.findParticipant(courseInSemester, user);
    }

    public List<CourseInSemester> getAllUsersCoursesInSemester(Semester semester, User user){
        Objects.requireNonNull(semester);
        Objects.requireNonNull(user);

        return courseInSemesterDao.findUsersCourses(semester, user);
    }
}

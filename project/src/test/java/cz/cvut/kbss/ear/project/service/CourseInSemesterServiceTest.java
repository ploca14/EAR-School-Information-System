package cz.cvut.kbss.ear.project.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import cz.cvut.kbss.ear.project.dao.CourseInSemesterDao;
import cz.cvut.kbss.ear.project.dao.CourseStudentDao;
import cz.cvut.kbss.ear.project.dao.CourseTeacherDao;
import cz.cvut.kbss.ear.project.enviroment.Generator;
import cz.cvut.kbss.ear.project.exception.CourseException;
import cz.cvut.kbss.ear.project.model.Course;
import cz.cvut.kbss.ear.project.model.CourseInSemester;
import cz.cvut.kbss.ear.project.model.Semester;
import cz.cvut.kbss.ear.project.model.User;
import cz.cvut.kbss.ear.project.model.enums.CourseCompletionType;
import cz.cvut.kbss.ear.project.model.enums.SemesterType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
public class CourseInSemesterServiceTest {

    @Autowired
    private CourseInSemesterService courseInSemesterService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private SemesterService semesterService;

    @Autowired
    private UserService userService;

    @Autowired
    private CourseInSemesterDao dao;

    @Autowired
    private CourseTeacherDao courseTeacherDao;

    @Autowired
    private CourseStudentDao courseStudentDao;

    @Test
    public void createNewCourseInSemester_createTwoInstancesInOneSemester_exceptionThrown() {
        Course course = courseService.createNewCourse("EAR", 5, "B36EAR", CourseCompletionType.KZ);
        Semester semester = semesterService.addNewSemester("B211", "2021", SemesterType.WINTER);

        courseInSemesterService.createNewCourseInSemester(course, semester);
        assertThrows(CourseException.class,
            () -> courseInSemesterService.createNewCourseInSemester(course, semester));
    }

    @Test
    public void enrolTeacherInCourse_createCourseTeacher_courseTeacherCreated() {
        Course course = courseService.createNewCourse("EAR", 5, "B36EAR", CourseCompletionType.KZ);
        Semester semester = semesterService.addNewSemester("B211", "2021", SemesterType.WINTER);
        User user = userService.persist(Generator.generateUser());
        CourseInSemester courseInSemester = courseInSemesterService.createNewCourseInSemester(
            course, semester);

        courseInSemesterService.enrolAsTeacherInCourse(user, courseInSemester);
        assertTrue(courseTeacherDao.findAllByUser(user).stream().findFirst().isPresent());
        assertEquals(courseTeacherDao.findAllByUser(user).stream().findFirst().get().getCourse(),
            courseInSemester);
        assertNotNull(dao.findCourseInSemester(course, semester).getTeachers().stream()
            .filter(t -> t.getUser().equals(user)).findFirst());
    }

    @Test
    public void enrolStudentInCourse_createCourseStudent_courseStudentCreated() {
        Course course = courseService.createNewCourse("EAR", 5, "B36EAR", CourseCompletionType.KZ);
        Semester semester = semesterService.addNewSemester("B211", "2021", SemesterType.WINTER);
        User user = userService.persist(Generator.generateUser());
        CourseInSemester courseInSemester = courseInSemesterService.createNewCourseInSemester(
            course, semester);

        courseInSemesterService.enrolAsStudentInCourse(user, courseInSemester);
        assertTrue(courseStudentDao.findAllByUser(user).stream().findFirst().isPresent());
        assertEquals(courseStudentDao.findAllByUser(user).stream().findFirst().get().getCourse(),
            courseInSemester);
    }

    @Test
    void unenrolFromCourse_unenrollCourseStudent_courseStudentUnenrolled() {
        Course course = courseService.createNewCourse("EAR", 5, "B36EAR", CourseCompletionType.KZ);
        Semester semester = semesterService.addNewSemester("B211", "2021", SemesterType.WINTER);
        User user = userService.persist(Generator.generateUser());
        CourseInSemester courseInSemester = courseInSemesterService.createNewCourseInSemester(
            course, semester);
        courseInSemesterService.enrolAsStudentInCourse(user, courseInSemester);

        courseInSemesterService.unenrolFromCourse(user, courseInSemester);
        assertEquals(0, courseStudentDao.findAllByUser(user).size());
    }

    @Test
    void unenrolFromCourse_unenrollCourseTeacher_courseTeacherUnenrolled() {
        Course course = courseService.createNewCourse("EAR", 5, "B36EAR", CourseCompletionType.KZ);
        Semester semester = semesterService.addNewSemester("B211", "2021", SemesterType.WINTER);
        User user = userService.persist(Generator.generateUser());
        CourseInSemester courseInSemester = courseInSemesterService.createNewCourseInSemester(
            course, semester);
        courseInSemesterService.enrolAsTeacherInCourse(user, courseInSemester);

        courseInSemesterService.unenrolFromCourse(user, courseInSemester);

        assertEquals(0, courseTeacherDao.findAllByUser(user).size());
        assertEquals(0, courseInSemester.getTeachers().size());
    }

    @Test
    void isUserEnroled_enrolledTeacher_true() {
        Course course = courseService.createNewCourse("EAR", 5, "B36EAR", CourseCompletionType.KZ);
        Semester semester = semesterService.addNewSemester("B211", "2021", SemesterType.WINTER);
        User user = userService.persist(Generator.generateUser());
        CourseInSemester courseInSemester = courseInSemesterService.createNewCourseInSemester(
            course, semester);
        courseInSemesterService.enrolAsTeacherInCourse(user, courseInSemester);

        final boolean result = courseInSemesterService.isUserEnroled(user, courseInSemester);
        assertTrue(result);
    }
}

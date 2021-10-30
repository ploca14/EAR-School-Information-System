package cz.cvut.kbss.ear.project.service;

import cz.cvut.kbss.ear.project.exception.CourseException;
import cz.cvut.kbss.ear.project.model.Course;
import cz.cvut.kbss.ear.project.model.Semester;
import cz.cvut.kbss.ear.project.model.enums.CourseCompletionType;
import cz.cvut.kbss.ear.project.model.enums.SemesterType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertThrows;

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

    @Test
    public void createNewCourseInSemester_createTwoInstancesInOneSemester_exceptionThrown() {
        Course course = courseService.createNewCourse("EAR", 5, "B36EAR", CourseCompletionType.KZ);
        Semester semester = semesterService.addNewSemester("B211", "2021", SemesterType.WINTER);

        courseInSemesterService.createNewCourseInSemester(course, semester);
        assertThrows(CourseException.class, () -> courseInSemesterService.createNewCourseInSemester(course, semester));
    }
}

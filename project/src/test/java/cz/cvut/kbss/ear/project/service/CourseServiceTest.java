package cz.cvut.kbss.ear.project.service;

import cz.cvut.kbss.ear.project.exception.CourseException;
import cz.cvut.kbss.ear.project.model.enums.CourseCompletionType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
public class CourseServiceTest {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private CourseService courseService;

    @Test
    public void createNewCourse_createTwoCoursesWithSameCode_CourseException(){
        courseService.createNewCourse("EAR", 5, "B36EAR", CourseCompletionType.KZ);

        assertThrows(CourseException.class, () -> courseService.createNewCourse("EAR", 5, "B36EAR", CourseCompletionType.KZ));
    }
}

package cz.cvut.kbss.ear.project.service;

import cz.cvut.kbss.ear.project.Application;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
public class CourseServiceTest {

    @PersistenceContext
    private EntityManager em;

    private CourseService courseService;

    @Test
    public void createNewCourse_createTwoCoursesWithSameCode_CourseException(){
        return;
    }
}

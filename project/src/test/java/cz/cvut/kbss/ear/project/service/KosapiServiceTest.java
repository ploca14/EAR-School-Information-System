package cz.cvut.kbss.ear.project.service;

import cz.cvut.kbss.ear.project.model.Course;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
public class KosapiServiceTest {

    @Autowired
    KosapiService kosapiService;

    @Test
    public void getCourse_getB6B36EAR_courseFetchedFromKosAPI(){
        String code = "B6B36EAR";
        Course course = kosapiService.getCourse(code);

        Assertions.assertNotNull(course);
    }

    @Test
    @Disabled // The test takes 30 seconds to execute
    public void getCoursesInSemester_B211semester_coursesFetchedFromKosAPI(){
        String semester_code = "B211";
        List<Course> courses = kosapiService.getCoursesInSemester(semester_code);

        Assertions.assertTrue(courses.size() > 2000);
    }
}

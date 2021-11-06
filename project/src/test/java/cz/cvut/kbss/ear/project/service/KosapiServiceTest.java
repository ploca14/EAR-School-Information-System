package cz.cvut.kbss.ear.project.service;

import cz.cvut.kbss.ear.project.kosapi.entities.KosCourse;
import cz.cvut.kbss.ear.project.kosapi.entities.KosTeacher;
import cz.cvut.kbss.ear.project.model.Course;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
public class KosapiServiceTest {

    @Autowired
    KosapiService kosapiService;

    @Test
    public void getCourseInSemester_getB6B36EAR_courseFetchedFromKosAPI(){
        String code = "B6B36EAR";
        String semesterCode = "B211";
        KosCourse course = kosapiService.getCourseInSemester(code, semesterCode);

        Assertions.assertNotNull(course);
    }

    @Test
    public void getCourseInSemester_tryToGetNotExistingCourse_httpClientExceptionThrown(){
        String code = "wieGehtsMann?";
        String semesterCode = "B211";
        Assertions.assertThrows(HttpClientErrorException.class,
                () -> kosapiService.getCourseInSemester(code, semesterCode));
    }

    @Test
    public void getAllCoursesInSemester_notExistingSemester_httpClientExceptionThrown(){
        String semester_code = "yoyoyoMann";
        Assertions.assertThrows(HttpClientErrorException.class,
                () -> kosapiService.getAllCoursesInSemester(semester_code));
    }

    @Test
    @Disabled // The test takes a minute to execute
    public void getAllCoursesInSemester_B211semester_coursesFetchedFromKosAPI(){
        String semester_code = "B211";
        List<KosCourse> courses = kosapiService.getAllCoursesInSemester(semester_code);

        Assertions.assertTrue(courses.size() > 2000);
    }

    @Test
    public void getTeachersInCourse_getEARInB211_LedvinkaInTeachers(){
        String code = "B6B36EAR";
        String semesterCode = "B211";
        Boolean ledvinkaFound = false;

        List<KosTeacher> teachers = kosapiService.getTeachersInCourse(code, semesterCode);
        for (KosTeacher teacher : teachers){
            if (teacher.getLastName().equals("Ledvinka")) ledvinkaFound = true;
        }

        Assertions.assertTrue(ledvinkaFound);
    }
}

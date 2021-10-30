package cz.cvut.kbss.ear.project.dao;

import cz.cvut.kbss.ear.project.Application;
import cz.cvut.kbss.ear.project.dao.ClassroomDao;
import cz.cvut.kbss.ear.project.model.Classroom;
import cz.cvut.kbss.ear.project.model.Course;
import cz.cvut.kbss.ear.project.model.enums.CourseCompletionType;
import cz.cvut.kbss.ear.project.service.CourseService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.ComponentScan;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@ComponentScan(basePackageClasses = Application.class)
public class CourseDaoTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private CourseDao courseDao;

    @Autowired
    private CourseService courseService;

    @Test
    public void findByCode_courseExists_returnsCourseWithMatchingCode() {
        courseService.createNewCourse("Kurz", 5, "B36EAR", CourseCompletionType.KZ);

        final Course result = courseDao.findByCode("B36EAR");

        assertNotNull(result);
        assertEquals("B36EAR", result.getCode());
    }
}

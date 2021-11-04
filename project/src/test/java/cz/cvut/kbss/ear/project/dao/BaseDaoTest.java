package cz.cvut.kbss.ear.project.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import cz.cvut.kbss.ear.project.Application;
import cz.cvut.kbss.ear.project.model.Classroom;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.ComponentScan;

@DataJpaTest
@ComponentScan(basePackageClasses = Application.class)
public class BaseDaoTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private ClassroomDao classroomDao;

    @Test
    public void persist_persistOneEntity_entityPersisted() {
        Classroom classroom = new Classroom();
        classroom.setName("Test classroom");
        classroomDao.persist(classroom);

        final Classroom result = em.find(Classroom.class, classroom.getId());

        assertNotNull(result);
        assertEquals(classroom.getId(), result.getId());
        assertEquals(classroom.getName(), result.getName());
    }
}
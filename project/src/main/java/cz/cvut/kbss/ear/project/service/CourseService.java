package cz.cvut.kbss.ear.project.service;

import cz.cvut.kbss.ear.project.dao.CourseDao;
import cz.cvut.kbss.ear.project.exception.CourseException;
import cz.cvut.kbss.ear.project.model.Course;
import cz.cvut.kbss.ear.project.model.enums.CourseCompletionType;
import java.util.List;
import java.util.Objects;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CourseService {

    private final CourseDao dao;

    public CourseService(CourseDao courseDao) {
        this.dao = courseDao;
    }

    @Transactional
    public Course createNewCourse(
        String name, Integer credits, String code,
        CourseCompletionType completionType
    ) {
        if (dao.findByCode(code) != null) {
            throw new CourseException("Course with this code already exists");
        }

        Course course = new Course();
        course.setName(name);
        course.setCredits(credits);
        course.setCode(code);
        course.setCompletionType(completionType);
        dao.persist(course);

        return course;
    }

    @Transactional
    public List<Course> findAll() {
        return dao.findAll();
    }

    @Transactional
    public Course find(Integer id) {
        return dao.find(id);
    }

    @Transactional
    public void persist(Course course) {
        Objects.requireNonNull(course);
        dao.persist(course);
    }
}

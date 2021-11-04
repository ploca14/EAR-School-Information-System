package cz.cvut.kbss.ear.project.service;

import cz.cvut.kbss.ear.project.dao.CourseDao;
import cz.cvut.kbss.ear.project.exception.CourseException;
import cz.cvut.kbss.ear.project.model.Course;
import cz.cvut.kbss.ear.project.model.enums.CourseCompletionType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CourseService {

    private final CourseDao courseDao;

    public CourseService(CourseDao courseDao) {
        this.courseDao = courseDao;
    }

    @Transactional
    public Course createNewCourse(String name, Integer credits, String code,
        CourseCompletionType completionType) {
        if (courseDao.findByCode(code) != null) {
            throw new CourseException("Course with this code already exists");
        }

        Course course = new Course();
        course.setName(name);
        course.setCredits(credits);
        course.setCode(code);
        course.setCompletionType(completionType);
        courseDao.persist(course);

        return course;
    }
}
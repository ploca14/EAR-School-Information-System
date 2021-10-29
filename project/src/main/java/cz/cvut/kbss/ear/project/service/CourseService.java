package cz.cvut.kbss.ear.project.service;

import cz.cvut.kbss.ear.project.dao.CourseDao;
import cz.cvut.kbss.ear.project.exception.CourseException;
import cz.cvut.kbss.ear.project.model.Course;
import cz.cvut.kbss.ear.project.model.enums.CourseCompletionType;
import org.springframework.stereotype.Service;

@Service
public class CourseService {

    private final CourseDao courseDao;

    public CourseService(CourseDao courseDao) {
        this.courseDao = courseDao;
    }

    public void createNewCourse(String name, Integer credits, String code, CourseCompletionType completionType){
        if (courseDao.findByCode(code) != null){
            throw new CourseException("Course with this code already exists");
        }

        Course course = new Course();
        course.setName(name);
        course.setCredits(credits);
        course.setCode(code);
        course.setCompletionType(completionType);

        courseDao.persist(course);
    }

    public void setRecommendedSemester(Course course, Integer semester){
        course.setRecommendedSemester(semester);
        courseDao.update(course);
    }

    public void setDescription(Course course, String description){
        course.setDescription(description);
        courseDao.update(course);
    }
}

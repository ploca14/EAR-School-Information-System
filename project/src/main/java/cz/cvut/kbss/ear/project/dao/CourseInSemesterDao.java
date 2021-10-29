package cz.cvut.kbss.ear.project.dao;

import cz.cvut.kbss.ear.project.model.Course;
import cz.cvut.kbss.ear.project.model.CourseInSemester;
import cz.cvut.kbss.ear.project.model.Semester;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;

@Repository
public class CourseInSemesterDao extends BaseDao<CourseInSemester>{
    public CourseInSemesterDao() {
        super(CourseInSemester.class);
    }

    public CourseInSemester findCourseInSemester(Course course, Semester semester){
        try {
            return em.createNamedQuery("CourseInSemester.findCourseInSemester", CourseInSemester.class)
                    .setParameter("course", course)
                    .setParameter("semester", semester)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}

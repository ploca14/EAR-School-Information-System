package cz.cvut.kbss.ear.project.dao;

import cz.cvut.kbss.ear.project.model.Course;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;

@Repository
public class CourseDao extends BaseDao<Course>{
    public CourseDao() {
        super(Course.class);
    }

    public Course findByCode(String code){
        try {
            return em.createNamedQuery("Course.findByCode", Course.class).setParameter("code", code)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}

package cz.cvut.kbss.ear.project.dao;

import cz.cvut.kbss.ear.project.model.Course;
import org.springframework.stereotype.Repository;

@Repository
public class CourseDao extends BaseDao<Course>{
    public CourseDao() {
        super(Course.class);
    }
}

package cz.cvut.kbss.ear.project.dao;

import cz.cvut.kbss.ear.project.model.CourseStudent;
import org.springframework.stereotype.Repository;

@Repository
public class CourseStudentDao extends BaseDao<CourseStudent>{
    public CourseStudentDao() {
        super(CourseStudent.class);
    }
}

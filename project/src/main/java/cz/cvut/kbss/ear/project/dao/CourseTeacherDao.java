package cz.cvut.kbss.ear.project.dao;

import cz.cvut.kbss.ear.project.model.CourseTeacher;
import org.springframework.stereotype.Repository;

@Repository
public class CourseTeacherDao extends BaseDao<CourseTeacher>{
    public CourseTeacherDao() {
        super(CourseTeacher.class);
    }
}

package cz.cvut.kbss.ear.project.dao;

import cz.cvut.kbss.ear.project.model.CourseInSemester;
import org.springframework.stereotype.Repository;

@Repository
public class CourseInSemesterDao extends BaseDao<CourseInSemester>{
    public CourseInSemesterDao() {
        super(CourseInSemester.class);
    }
}

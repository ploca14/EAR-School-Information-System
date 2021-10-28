package cz.cvut.kbss.ear.project.dao;

import cz.cvut.kbss.ear.project.model.Semester;
import org.springframework.stereotype.Repository;

@Repository
public class SemesterDao extends BaseDao<Semester>{
    public SemesterDao() {
        super(Semester.class);
    }
}

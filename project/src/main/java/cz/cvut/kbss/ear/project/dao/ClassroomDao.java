package cz.cvut.kbss.ear.project.dao;

import cz.cvut.kbss.ear.project.model.Classroom;
import org.springframework.stereotype.Repository;

@Repository
public class ClassroomDao extends BaseDao<Classroom> {

    public ClassroomDao() {
        super(Classroom.class);
    }
}

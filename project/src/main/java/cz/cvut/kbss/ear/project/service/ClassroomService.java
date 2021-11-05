package cz.cvut.kbss.ear.project.service;

import cz.cvut.kbss.ear.project.dao.ClassroomDao;
import cz.cvut.kbss.ear.project.model.Classroom;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClassroomService {

    private final ClassroomDao dao;

    @Autowired
    public ClassroomService(ClassroomDao dao) {
        this.dao = dao;
    }

    @Transactional
    public List<Classroom> findAll() {
        return dao.findAll();
    }

    @Transactional
    public Classroom find(Integer id) {
        return dao.find(id);
    }

    @Transactional
    public void persist(Classroom classroom) {
        Objects.requireNonNull(classroom);
        dao.persist(classroom);
    }
}

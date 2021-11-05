package cz.cvut.kbss.ear.project.service;

import cz.cvut.kbss.ear.project.dao.ParallelDao;
import cz.cvut.kbss.ear.project.exception.EnrolmentException;
import cz.cvut.kbss.ear.project.model.CourseInSemester;
import cz.cvut.kbss.ear.project.model.CourseParticipant;
import cz.cvut.kbss.ear.project.model.Parallel;
import java.util.List;
import java.util.Objects;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ParallelService {

    private final ParallelDao dao;

    public ParallelService(ParallelDao dao) {
        this.dao = dao;
    }

    @Transactional
    public List<Parallel> findAll() {
        return dao.findAll();
    }

    @Transactional
    public Parallel find(Integer id) {
        return dao.find(id);
    }

    @Transactional
    public void persist(Parallel parallel) {
        Objects.requireNonNull(parallel);
        dao.persist(parallel);
    }

    @Transactional
    public void enrollInParallel(CourseParticipant participant, Parallel parallel) {
        if (parallel.getCapacity() <= parallel.getCourseStudents().size()) {
            throw new EnrolmentException(
                String.format("The parallel %s is already full", parallel.getName()));
        }

        participant.enrollInParallel(parallel);
        dao.update(parallel);
    }

    @Transactional
    public void unenrollFromParallel(CourseParticipant participant, Parallel parallel) {
        participant.unenrollFromParallel(parallel);
    }

    @Transactional
    public void addParallelToCourse(Parallel parallel, CourseInSemester course) {
        course.addParallel(parallel);
        dao.persist(parallel);
    }

    @Transactional
    public void removeParallelFromCourse(Parallel parallel) {
        parallel.getCourseInSemester().removeParallel(parallel);
        dao.persist(parallel);
    }
}

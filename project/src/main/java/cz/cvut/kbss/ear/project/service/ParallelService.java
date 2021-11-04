package cz.cvut.kbss.ear.project.service;

import cz.cvut.kbss.ear.project.dao.ParallelDao;
import cz.cvut.kbss.ear.project.exception.EnrolmentException;
import cz.cvut.kbss.ear.project.model.CourseInSemester;
import cz.cvut.kbss.ear.project.model.CourseParticipant;
import cz.cvut.kbss.ear.project.model.Parallel;
import org.springframework.stereotype.Service;

@Service
public class ParallelService {

    private final ParallelDao dao;

    public ParallelService(ParallelDao dao) {
        this.dao = dao;
    }

    public void enrollInParallel(CourseParticipant participant, Parallel parallel) {
        if (parallel.getCapacity() <= parallel.getCourseStudents().size()) {
            throw new EnrolmentException(
                String.format("The parallel %s is already full", parallel.getName()));
        }

        participant.enrollInParallel(parallel);
        dao.update(parallel);
    }

    public void unenrollFromParallel(CourseParticipant participant, Parallel parallel) {
        participant.unenrollFromParallel(parallel);
    }

    public void addParallelToCourse(Parallel parallel, CourseInSemester course) {
        course.addParallel(parallel);
        dao.persist(parallel);
    }

    public void removeParallelFromCourse(Parallel parallel) {
        parallel.getCourseInSemester().removeParallel(parallel);
        dao.persist(parallel);
    }
}

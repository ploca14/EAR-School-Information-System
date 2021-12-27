package cz.cvut.kbss.ear.project.service;

import cz.cvut.kbss.ear.project.dao.ParallelDao;
import cz.cvut.kbss.ear.project.exception.EnrolmentException;
import cz.cvut.kbss.ear.project.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ParallelService {

    private final ParallelDao dao;

    private final ClassroomService classroomService;

    private final CourseInSemesterService courseInSemesterService;

    public ParallelService(ParallelDao dao, ClassroomService classroomService, CourseInSemesterService courseInSemesterService) {
        this.dao = dao;
        this.classroomService = classroomService;
        this.courseInSemesterService = courseInSemesterService;
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
        participant.enrollInParallel(parallel);
        dao.update(parallel);

        /**
         * I commented the check out, because parallels in kos do not respect the capacity.
         * For exmaple EAR 101 parallel has capacity = 20, but 21 students
         *
        if (parallel.getCapacity() <= parallel.getCourseStudents().size()) {
            throw new EnrolmentException(
                String.format("The parallel %s is already full", parallel.getName()));
        }*/
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
    public void removeParallelFromCourse(Parallel parallel) { // TODO check, that it cascades the remove to the whole parallel content
        parallel.getCourseInSemester().removeParallel(parallel);
        dao.persist(parallel);
    }

    public boolean isUserEnroled(Parallel parallel, User user){
        for (CourseParticipant parallelParticipant : parallel.getAllParticipants()){
            if (parallelParticipant.getUser().getUsername().equals(user.getUsername())) return true;
        }

        return false;
    }

    public List<Parallel> getUsersParallelsInSemester(User user, Semester semester){
        List<CourseInSemester> usersCourses = courseInSemesterService.getAllUsersCoursesInSemester(semester, user);
        List<Parallel> result = new ArrayList<>();
        for (CourseInSemester courseInSemester : usersCourses){
            for (Parallel parallel : courseInSemester.getParallels()){
                boolean userInParallel = parallel.getAllParticipants()
                        .stream()
                        .map(CourseParticipant::getUser)
                        .collect(Collectors.toList())
                        .contains(user);
                if (userInParallel) result.add(parallel);
            }
        }

        return result;
    }
}

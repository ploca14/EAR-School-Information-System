package cz.cvut.kbss.ear.project.service;

import cz.cvut.kbss.ear.project.kosapi.entities.*;
import cz.cvut.kbss.ear.project.model.CourseInSemester;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

/**
 * Synchronises parallels and parallels in a course based on KOS.
 */
@Service
public class CourseSynchronisationService {

    private final CourseInSemesterService courseInSemesterService;

    private final UserService userService;

    private final KosapiService kosapiService;

    private KosCourse kosCourse;

    private List<KosStudent> kosStudentsInCourse;

    private List<KosTeacher> kosTeachersInCourse;

    private List<KosParallel> kosParallels;

    private HashMap<KosParallel, List<KosStudent>> parallelStudents;

    private HashMap<KosParallel, List<KosTeacher>> parallelTeachers;

    public CourseSynchronisationService(CourseInSemesterService courseInSemesterService,
                                        KosapiService kosapiService, UserService userService) {
        this.courseInSemesterService = courseInSemesterService;
        this.kosapiService = kosapiService;
        this.userService = userService;
    }

    @Transactional
    public void synchroniseWithKos(CourseInSemester courseInSemester){
        // TODO mozna pridat flag do modelu, jestli byl kurz importovan z KOSu
        loadData(courseInSemester);
        synchroniseCourseEnrolments();
        synchroniseParallelEnrolments();
    }

    private void synchroniseCourseEnrolments(){

    }

    private void synchroniseParallelEnrolments(){

    }

    private void loadData(CourseInSemester courseInSemester){
        kosCourse = kosapiService.getCourseInSemester(courseInSemester.getCourse().getCode(),
                courseInSemester.getSemester().getCode());
        kosStudentsInCourse = kosapiService.getStudentsInCourse(kosCourse);
        kosTeachersInCourse = kosapiService.getTeachersInCourse(kosCourse);
        kosParallels = kosapiService.getParallelsInCourse(kosCourse.getCode(), courseInSemester.getSemester().getCode());

        for (KosParallel parallel : kosParallels){
            parallelTeachers.put(parallel, kosapiService.getTeachersInParallel(parallel));
            parallelStudents.put(parallel, kosapiService.getStudentsInParallel(parallel));
        }
    }
}

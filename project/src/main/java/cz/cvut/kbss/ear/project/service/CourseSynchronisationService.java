package cz.cvut.kbss.ear.project.service;

import cz.cvut.kbss.ear.project.kosapi.entities.*;
import cz.cvut.kbss.ear.project.model.CourseInSemester;
import cz.cvut.kbss.ear.project.model.CourseParticipant;
import cz.cvut.kbss.ear.project.model.Parallel;
import cz.cvut.kbss.ear.project.model.User;
import cz.cvut.kbss.ear.project.service.util.KosapiEntityConverter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
        loadDataFromKosapi(courseInSemester);
        synchroniseCourseEnrolments(courseInSemester);
        synchroniseParallelEnrolments(courseInSemester);
    }

    private void synchroniseCourseEnrolments(CourseInSemester courseInSemester){
        List<User> students = convertStudents(kosStudentsInCourse);
        List<User> teachers = convertTeachers(kosTeachersInCourse);
        enrolNewUsersInCourse(students, teachers, courseInSemester);

        List<User> allParticipantsFromKos = students;
        allParticipantsFromKos.addAll(teachers);

        unenrolOldUsersFromCourse(allParticipantsFromKos, courseInSemesterService.getAllParticipants(courseInSemester));
    }

    private void enrolNewUsersInCourse(List<User> students, List<User> teachers, CourseInSemester courseInSemester){
        for (User student : students){
            if (!courseInSemesterService.isUserEnroled(student, courseInSemester)){
                courseInSemesterService.enrolAsStudentInCourse(student, courseInSemester);
            }
        }

        for (User teacher : teachers){
            if (!courseInSemesterService.isUserEnroled(teacher, courseInSemester)){
                courseInSemesterService.enrolAsTeacherInCourse(teacher, courseInSemester);
            }
        }
    }

    private void unenrolOldUsersFromCourse(List<User> participantsFromKos, List<CourseParticipant> currentParticipants){
        boolean foundInKos;
        for (CourseParticipant currentParticipant : currentParticipants){
            foundInKos = false;
            for (User participantKos : participantsFromKos){
                if (participantKos.getUsername().equals(currentParticipant.getUser().getUsername())){
                    foundInKos = true;
                    break;
                }
            }

            if (!foundInKos){
                courseInSemesterService.unenrolFromCourse(currentParticipant.getUser(), currentParticipant.getCourse());
            }
        }
    }

    private void enrolTeacherInParallel(User teacher, Parallel parallel){

    }

    private void enrolStudentInParallel(User student, Parallel parallel){

    }

    private List<User> convertTeachers(List<KosTeacher> kosTeachers){
        List<User> convertedUsers = new ArrayList<>();

        for (KosParticipant kosParticipant : kosTeachers){
            convertedUsers.add(createOrGetUser(kosParticipant));
        }

        return convertedUsers;
    }

    private List<User> convertStudents(List<KosStudent> kosStudents){
        List<User> convertedUsers = new ArrayList<>();

        for (KosParticipant kosParticipant : kosStudents){
            convertedUsers.add(createOrGetUser(kosParticipant));
        }

        return convertedUsers;
    }

    private void synchroniseParallelEnrolments(CourseInSemester courseInSemester){
        for (KosParallel kosParallel : kosParallels){
            Parallel parallel = KosapiEntityConverter.kosParallelToParallel(kosParallel);
            List<KosStudent> studentsInParallel = parallelStudents.get(kosParallel);
            List<KosTeacher> teachersInParallel = parallelTeachers.get(kosParallel);

            enrolNewUsersInParallel(parallel, studentsInParallel, teachersInParallel);
        }
    }

    private void enrolNewUsersInParallel(Parallel parallel, List<KosStudent> kosStudents, List<KosTeacher> kosTeachers){
        for (KosStudent student : kosStudents){
            User user = createOrGetUser(student);
        }
    }

    private User createOrGetUser(KosParticipant kosParticipant){
        // TODO cache?
        User user = KosapiEntityConverter.kosParticipantToUser(kosParticipant);
        if (!userService.exists(user.getUsername())){
            userService.persist(user);
        }
        return userService.findByUsername(user.getUsername());
    }

    private void loadDataFromKosapi(CourseInSemester courseInSemester){
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

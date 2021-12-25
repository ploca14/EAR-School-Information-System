package cz.cvut.kbss.ear.project.rest.controllers;

import cz.cvut.kbss.ear.project.exception.CourseException;
import cz.cvut.kbss.ear.project.exception.SemesterException;
import cz.cvut.kbss.ear.project.kosapi.entities.KosCourse;
import cz.cvut.kbss.ear.project.model.*;
import cz.cvut.kbss.ear.project.rest.dto.CourseInSemesterDTO;
import cz.cvut.kbss.ear.project.rest.dto.ParallelDTO;
import cz.cvut.kbss.ear.project.rest.util.Code;
import cz.cvut.kbss.ear.project.rest.util.RestUtils;
import cz.cvut.kbss.ear.project.service.*;
import cz.cvut.kbss.ear.project.service.util.KosapiEntityConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Resources
 * /api/courses DONE
 * - POST: DONE
 *      - manualni vytvoreni, bude brat Course entity DONE
 * /api/courses/kos (Tady neni GET)
 * - POST: DONE
 *     - import z kosu DONE
 * /api/courses/code DONE
 * /api/courses/code/semesterCode DONE
 * - POST: DONE
 *    - vytvor instanci kurzu v semestru na zaklade course DONE
 *
 * - /api/courses/code/semesterCode/kos DONE
 * - POST:
 *     - import veci z KOSu DONE
 * /api/courses/code/semesterCode/participants DONE
 * /api/courses/code/semesterCode/participants/students DONE
 * - POST:
 *      - enrol
 * - DELETE:
 *      - unenrol
 * /api/courses/code/semesterCode/participants/teachers DONE
 * - POST:
 *      - enrol
 * - DELETE:
 *      - unenrol
 * /api/courses/code/semesterCode/parallels DONE
 * - POST:
 *      - enrol
 *      - create parallel
 * - DELETE:
 *      - unenrol
 *      - remove parallel
 **/
@RestController
@RequestMapping("/api/courses")
// TODO security
public class CourseController {

    private static final Logger LOG = LoggerFactory.getLogger(CourseController.class);

    private final CourseService courseService;

    private final CourseInSemesterService courseInSemesterService;

    private final CourseSynchronisationService courseSynchronisationService;

    private final SemesterService semesterService;

    private final ParallelService parallelService;

    private final KosapiService kosapiService;

    public CourseController(CourseService courseService, CourseInSemesterService courseInSemesterService,
                            SemesterService semesterService, ParallelService parallelService, KosapiService kosapiService,
                            CourseSynchronisationService courseSynchronisationService) {
        this.courseService = courseService;
        this.courseInSemesterService = courseInSemesterService;
        this.semesterService = semesterService;
        this.parallelService = parallelService;
        this.kosapiService = kosapiService;
        this.courseSynchronisationService = courseSynchronisationService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Course> getCourses() {
        return courseService.findAll();
    }

    @GetMapping(value = "/{code}",produces = MediaType.APPLICATION_JSON_VALUE)
    public Course getCourseByCode(@PathVariable String code) {
        return courseService.findByCode(code);
    }

    @GetMapping(value = "/{courseCode}/{semesterCode}/participants",produces = MediaType.APPLICATION_JSON_VALUE)
    public List<User> getUsersInCourse(@PathVariable String courseCode, @PathVariable String semesterCode) {
        CourseInSemester courseInSemester = courseInSemesterService.findByCode(courseCode, semesterCode);
        List<User> usersInCourse = courseInSemesterService.getAllParticipants(courseInSemester)
                .stream()
                .map((CourseParticipant::getUser))
                .collect(Collectors.toList());
        return usersInCourse;
    }

    @GetMapping(value = "/{courseCode}/{semesterCode}/participants/students",produces = MediaType.APPLICATION_JSON_VALUE)
    public List<User> getStudentsInCourse(@PathVariable String courseCode, @PathVariable String semesterCode) {
        CourseInSemester courseInSemester = courseInSemesterService.findByCode(courseCode, semesterCode);
        List<User> usersInCourse = courseInSemesterService.getStudents(courseInSemester)
                .stream()
                .map((CourseParticipant::getUser))
                .collect(Collectors.toList());
        return usersInCourse;
    }

    @GetMapping(value = "/{courseCode}/{semesterCode}/participants/teachers",produces = MediaType.APPLICATION_JSON_VALUE)
    public List<User> getTeachersIncourse(@PathVariable String courseCode, @PathVariable String semesterCode) {
        CourseInSemester courseInSemester = courseInSemesterService.findByCode(courseCode, semesterCode);
        List<User> usersInCourse = courseInSemesterService.getTeachers(courseInSemester)
                .stream()
                .map((CourseParticipant::getUser))
                .collect(Collectors.toList());
        return usersInCourse;
    }

    @GetMapping(value = "/{courseCode}/{semesterCode}/parallels", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ParallelDTO> getParallelInCourse(@PathVariable String courseCode, @PathVariable String semesterCode) {
        CourseInSemester courseInSemester = courseInSemesterService.findByCode(courseCode, semesterCode);
        List<ParallelDTO> parallelsInCourse = courseInSemesterService.getParallels(courseInSemester)
                .stream()
                .map(ParallelDTO::new)
                .collect(Collectors.toList());
        return parallelsInCourse;
    }

    @GetMapping(value = "/{code}/{semesterCode}",produces = MediaType.APPLICATION_JSON_VALUE)
    public CourseInSemesterDTO getCourseInSemester(@PathVariable String code, @PathVariable String semesterCode) {
        CourseInSemester courseInSemester = courseInSemesterService.findByCode(code, semesterCode);
        return new CourseInSemesterDTO(courseInSemester);
    }

    @PostMapping(value = "/{courseCode}/{semesterCode}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createCourseInSemester(@PathVariable String courseCode, @PathVariable String semesterCode) {
        Course course = courseService.findByCode(courseCode);
        if (course == null){
            throw new CourseException("Course with code " + courseCode + " does not exist, therefore an instance in a semester" +
                    "cannot be created. Create the course first by POST on api/courses.");
        }
        Semester semester = semesterService.findByCode(semesterCode);

        if (semester == null){
            throw new SemesterException("Semester with a given code does not exist.");
        }

        courseInSemesterService.addCourseToSemester(course, semester);
        LOG.debug("Created course with {} \n " + "in semester {}.", course, semester);
        final HttpHeaders headers = RestUtils.createLocationHeaderFromCurrentUri("/{code}/{semesterCode}", course.getCode(), semesterCode);
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createCourse(@RequestBody Course course) {
        courseService.persist(course);
        LOG.debug("Created course {}.", course);
        final HttpHeaders headers = RestUtils.createLocationHeaderFromCurrentUri("/{code}", course.getCode());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @PostMapping(value = "/kos", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createCourseFromKos(@RequestBody Code code) {
        KosCourse kosCourse = kosapiService.getCourse(code.getCode());
        courseService.persist(KosapiEntityConverter.kosCourseToCourse(kosCourse));
        LOG.debug("Created course from kos course {}.", kosCourse);
        final HttpHeaders headers = RestUtils.createLocationHeaderFromCurrentUri("/{code}", kosCourse.getCode()); // TODO incorrect URI
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @PostMapping(value = "/{courseCode}/{semesterCode}/kos")
    public ResponseEntity<Void> synchroniseCourseInSemesterWithKos(@PathVariable String courseCode, @PathVariable String semesterCode) {
        CourseInSemester courseInSemester = courseInSemesterService.findByCode(courseCode, semesterCode);
        courseSynchronisationService.synchroniseWithKos(courseInSemester);
        LOG.debug("Synchronised course {}.", courseInSemester);
        final HttpHeaders headers = RestUtils.createLocationHeaderFromCurrentUri("/{courseCode}/{semesterCode}/parallels", courseCode, semesterCode);
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }
}

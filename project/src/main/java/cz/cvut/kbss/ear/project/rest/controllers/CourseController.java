package cz.cvut.kbss.ear.project.rest.controllers;

import cz.cvut.kbss.ear.project.exception.CourseException;
import cz.cvut.kbss.ear.project.exception.NotFoundException;
import cz.cvut.kbss.ear.project.exception.SemesterException;
import cz.cvut.kbss.ear.project.kosapi.entities.KosCourse;
import cz.cvut.kbss.ear.project.model.*;
import cz.cvut.kbss.ear.project.rest.dto.CourseInSemesterDTO;
import cz.cvut.kbss.ear.project.rest.dto.ParallelDTO;
import cz.cvut.kbss.ear.project.rest.dto.UsernameDTO;
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

import static cz.cvut.kbss.ear.project.rest.util.DTOConverter.dtoToParallel;

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
 * - DELETE:
 *       - unenrol DONE
 * /api/courses/code/semesterCode/participants/students DONE
 * - POST:
 *      - enrol DONE
 * - DELETE:
 *      - unenrol DONE
 * /api/courses/code/semesterCode/participants/teachers DONE
 * - POST:
 *      - enrol DONE
 * - DELETE:
 *      - unenrol DONE
 * /api/courses/code/semesterCode/parallels DONE
 * - POST:
 *      - create parallel DONE
 *
 * /api/courses/code/semesterCode/parallels/id  DONE
 * - DELETE:
 *      - remove parallel DONE
 * /api/courses/code/semesterCode/parallels/id/participants DONE
 * - POST:
 *      - enrol
 * - DELETE:
 *      - unenrol
 *
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

    private final UserService userService;

    public CourseController(CourseService courseService, CourseInSemesterService courseInSemesterService,
                            SemesterService semesterService, ParallelService parallelService, KosapiService kosapiService,
                            CourseSynchronisationService courseSynchronisationService, UserService userService) {
        this.courseService = courseService;
        this.courseInSemesterService = courseInSemesterService;
        this.semesterService = semesterService;
        this.parallelService = parallelService;
        this.kosapiService = kosapiService;
        this.courseSynchronisationService = courseSynchronisationService;
        this.userService = userService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Course> getCourses() {
        return courseService.findAll();
    }

    @GetMapping(value = "/{code}",produces = MediaType.APPLICATION_JSON_VALUE)
    public Course getCourseByCode(@PathVariable String code) {
        Course course = courseService.findByCode(code);
        if (course == null){
            throw NotFoundException.create("Course", code);
        }
        return course;
    }

    @GetMapping(value = "/{courseCode}/{semesterCode}/participants",produces = MediaType.APPLICATION_JSON_VALUE)
    public List<User> getUsersInCourse(@PathVariable String courseCode, @PathVariable String semesterCode) {
        CourseInSemester courseInSemester = courseInSemesterService.findByCode(courseCode, semesterCode);
        if (courseInSemester == null){
            throw NotFoundException.create("CourseInSemester", "Coursecode:" + courseCode + ", SemesterCode: " + semesterCode);
        }
        return courseInSemesterService.getAllParticipants(courseInSemester)
                .stream()
                .map((CourseParticipant::getUser))
                .collect(Collectors.toList());
    }

    @GetMapping(value = "/{courseCode}/{semesterCode}/participants/students",produces = MediaType.APPLICATION_JSON_VALUE)
    public List<User> getStudentsInCourse(@PathVariable String courseCode, @PathVariable String semesterCode) {
        CourseInSemester courseInSemester = courseInSemesterService.findByCode(courseCode, semesterCode);
        if (courseInSemester == null){
            throw NotFoundException.create("CourseInSemester", "Coursecode:" + courseCode + ", SemesterCode: " + semesterCode);
        }
        return courseInSemesterService.getStudents(courseInSemester)
                .stream()
                .map((CourseParticipant::getUser))
                .collect(Collectors.toList());
    }

    @GetMapping(value = "/{courseCode}/{semesterCode}/participants/teachers",produces = MediaType.APPLICATION_JSON_VALUE)
    public List<User> getTeachersIncourse(@PathVariable String courseCode, @PathVariable String semesterCode) {
        CourseInSemester courseInSemester = courseInSemesterService.findByCode(courseCode, semesterCode);
        if (courseInSemester == null){
            throw NotFoundException.create("CourseInSemester", "Coursecode:" + courseCode + ", SemesterCode: " + semesterCode);
        }
        return courseInSemesterService.getTeachers(courseInSemester)
                .stream()
                .map((CourseParticipant::getUser))
                .collect(Collectors.toList());
    }

    @GetMapping(value = "/{courseCode}/{semesterCode}/parallels", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ParallelDTO> getParallelsInCourse(@PathVariable String courseCode, @PathVariable String semesterCode) {
        CourseInSemester courseInSemester = courseInSemesterService.findByCode(courseCode, semesterCode);
        if (courseInSemester == null){
            throw NotFoundException.create("CourseInSemester", "Coursecode:" + courseCode + ", SemesterCode: " + semesterCode);
        }
        return courseInSemesterService.getParallels(courseInSemester)
                .stream()
                .map(ParallelDTO::new)
                .collect(Collectors.toList());
    }

    @GetMapping(value = "/{code}/{semesterCode}",produces = MediaType.APPLICATION_JSON_VALUE)
    public CourseInSemesterDTO getCourseInSemester(@PathVariable String code, @PathVariable String semesterCode) {
        CourseInSemester courseInSemester = courseInSemesterService.findByCode(code, semesterCode);
        if (courseInSemester == null){
            throw NotFoundException.create("CourseInSemester", "Coursecode:" + code + ", SemesterCode: " + semesterCode);
        }
        return new CourseInSemesterDTO(courseInSemester);
    }

    @PostMapping(value = "/{courseCode}/{semesterCode}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createCourseInSemester(@PathVariable String courseCode, @PathVariable String semesterCode) {
        Course course = courseService.findByCode(courseCode);
        if (course == null){
            throw new NotFoundException("Course with code " + courseCode + " does not exist, therefore an instance in a semester" +
                    "cannot be created. Create the course first by POST on api/courses.");
        }
        Semester semester = semesterService.findByCode(semesterCode);

        if (semester == null){
            throw new NotFoundException("Semester with a given code does not exist.");
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
        if (courseInSemester == null) {
            throw NotFoundException.create("CourseInSemester", "Coursecode:" + courseCode + ", SemesterCode: " + semesterCode);
        }
        courseSynchronisationService.synchroniseWithKos(courseInSemester);
        LOG.debug("Synchronised course {}.", courseInSemester);
        final HttpHeaders headers = RestUtils.createLocationHeaderFromCurrentUri("/{courseCode}/{semesterCode}/parallels", courseCode, semesterCode);
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @PostMapping(value = "/{courseCode}/{semesterCode}/participants/teachers")
    public ResponseEntity<Void> enrolAsTeacher(@PathVariable String courseCode, @PathVariable String semesterCode, @RequestBody UsernameDTO usernameDTO) {
        CourseInSemester courseInSemester = courseInSemesterService.findByCode(courseCode, semesterCode);
        if (courseInSemester == null) {
            throw NotFoundException.create("CourseInSemester", "Coursecode:" + courseCode + ", SemesterCode: " + semesterCode);
        }
        User user = userService.findByUsername(usernameDTO.getUsername());
        if (user == null){
            throw NotFoundException.create("User", usernameDTO.getUsername());
        }
        courseInSemesterService.enrolAsTeacherInCourse(user, courseInSemester);
        LOG.debug("Enroled {} as teacher in course {}.", user.getUsername(), courseInSemester);
        final HttpHeaders headers = RestUtils.createLocationHeaderFromCurrentUri("/{courseCode}/{semesterCode}/participants/teachers", courseCode, semesterCode);
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @PostMapping(value = "/{courseCode}/{semesterCode}/participants/students")
    public ResponseEntity<Void> enrolAsStudent(@PathVariable String courseCode, @PathVariable String semesterCode, @RequestBody UsernameDTO usernameDTO) {
        CourseInSemester courseInSemester = courseInSemesterService.findByCode(courseCode, semesterCode);
        if (courseInSemester == null) {
            throw NotFoundException.create("CourseInSemester", "Coursecode:" + courseCode + ", SemesterCode: " + semesterCode);
        }
        User user = userService.findByUsername(usernameDTO.getUsername());
        if (user == null){
            throw NotFoundException.create("User", usernameDTO.getUsername());
        }
        courseInSemesterService.enrolAsStudentInCourse(user, courseInSemester);
        LOG.debug("Enroled {} as student in course {}.", user.getUsername(), courseInSemester);
        final HttpHeaders headers = RestUtils.createLocationHeaderFromCurrentUri("/{courseCode}/{semesterCode}/participants/students", courseCode, semesterCode);
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @DeleteMapping(value = {"/{courseCode}/{semesterCode}/participants/teachers", "/{courseCode}/{semesterCode}/participants/students", "/{courseCode}/{semesterCode}/participants"})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void unenrolFromCourse(@PathVariable String courseCode, @PathVariable String semesterCode, @RequestBody UsernameDTO usernameDTO) {
        CourseInSemester courseInSemester = courseInSemesterService.findByCode(courseCode, semesterCode);
        if (courseInSemester == null) {
            throw NotFoundException.create("CourseInSemester", "Coursecode:" + courseCode + ", SemesterCode: " + semesterCode);
        }
        User user = userService.findByUsername(usernameDTO.getUsername());
        if (user == null){
            throw NotFoundException.create("User", usernameDTO.getUsername());
        }
        courseInSemesterService.unenrolFromCourse(user, courseInSemester);
        LOG.debug("Unenroled {} from course {}.", user.getUsername(), courseInSemester);
    }

    @PostMapping(value = "/{courseCode}/{semesterCode}/parallels")
    public ResponseEntity<Void> createParallel(@PathVariable String courseCode, @PathVariable String semesterCode, @RequestBody ParallelDTO parallelDTO) {
        CourseInSemester courseInSemester = courseInSemesterService.findByCode(courseCode, semesterCode);
        if (courseInSemester == null) {
            throw NotFoundException.create("CourseInSemester", "Coursecode:" + courseCode + ", SemesterCode: " + semesterCode);
        }
        Parallel parallel = dtoToParallel(parallelDTO);
        parallelService.addParallelToCourse(parallel, courseInSemester);
        LOG.debug("Added parallel {} to course {}.", parallel, courseInSemester);
        final HttpHeaders headers = RestUtils.createLocationHeaderFromCurrentUri("/{courseCode}/{semesterCode}/parallels/{parallelId}", courseCode, semesterCode, parallel.getId());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/{courseCode}/{semesterCode}/parallels/{parallelId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeParallel(@PathVariable String courseCode, @PathVariable String semesterCode, @PathVariable Integer parallelId) {
        CourseInSemester courseInSemester = courseInSemesterService.findByCode(courseCode, semesterCode);
        if (courseInSemester == null) {
            throw NotFoundException.create("CourseInSemester", "Coursecode:" + courseCode + ", SemesterCode: " + semesterCode);
        }
        Parallel parallel = parallelService.find(parallelId);
        if (parallel == null) {
            throw NotFoundException.create("ParallelL", parallelId);
        }
        parallelService.removeParallelFromCourse(parallel);
        LOG.debug("Removed parallel {} from course {}.", parallel, courseInSemester);
    }

    @GetMapping(value = "/{courseCode}/{semesterCode}/parallels/{parallelId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ParallelDTO getParallelById(@PathVariable String courseCode, @PathVariable String semesterCode, @PathVariable Integer parallelId) {
        CourseInSemester courseInSemester = courseInSemesterService.findByCode(courseCode, semesterCode);
        if (courseInSemester == null) {
            throw NotFoundException.create("CourseInSemester", "Coursecode:" + courseCode + ", SemesterCode: " + semesterCode);
        }
        Parallel parallel = parallelService.find(parallelId);
        if (parallel == null) {
            throw NotFoundException.create("ParallelL", parallelId);
        }

        return new ParallelDTO(parallel);
    }

    @GetMapping(value = "/{courseCode}/{semesterCode}/parallels/{parallelId}/participants", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<User> getParallelUsers(@PathVariable String courseCode, @PathVariable String semesterCode, @PathVariable Integer parallelId) {
        CourseInSemester courseInSemester = courseInSemesterService.findByCode(courseCode, semesterCode);
        if (courseInSemester == null) {
            throw NotFoundException.create("CourseInSemester", "Coursecode:" + courseCode + ", SemesterCode: " + semesterCode);
        }
        Parallel parallel = parallelService.find(parallelId);
        if (parallel == null) {
            throw NotFoundException.create("ParallelL", parallelId);
        }
        return parallel.getAllParticipants()
                .stream()
                .map(CourseParticipant::getUser)
                .collect(Collectors.toList());
    }

    @DeleteMapping(value = "/{courseCode}/{semesterCode}/parallels/{parallelId}/participants")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void unenrolFromParallel(@PathVariable String courseCode, @PathVariable String semesterCode, @PathVariable Integer parallelId, @RequestBody UsernameDTO usernameDTO) {
        CourseInSemester courseInSemester = courseInSemesterService.findByCode(courseCode, semesterCode);
        if (courseInSemester == null) {
            throw NotFoundException.create("CourseInSemester", "Coursecode:" + courseCode + ", SemesterCode: " + semesterCode);
        }
        Parallel parallel = parallelService.find(parallelId);
        if (parallel == null) {
            throw NotFoundException.create("ParallelL", parallelId);
        }
        User user = userService.findByUsername(usernameDTO.getUsername());
        if (user == null){
            throw NotFoundException.create("User", usernameDTO.getUsername());
        }
        CourseParticipant courseParticipant = courseInSemesterService.getCourseParticipant(courseInSemester, user);
        if (courseParticipant == null){
            throw NotFoundException.create("CourseParticipant", usernameDTO.getUsername());
        }
        parallelService.unenrollFromParallel(courseParticipant, parallel);
        LOG.debug("Unenrolled user {} from parallel {} in course {}.", user, parallel, courseInSemester);
    }
}

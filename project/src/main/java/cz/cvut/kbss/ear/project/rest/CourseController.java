package cz.cvut.kbss.ear.project.rest;

import cz.cvut.kbss.ear.project.kosapi.entities.KosCourse;
import cz.cvut.kbss.ear.project.model.Course;
import cz.cvut.kbss.ear.project.model.CourseInSemester;
import cz.cvut.kbss.ear.project.rest.util.RestUtils;
import cz.cvut.kbss.ear.project.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Resources
 * /api/courses
 * - POST:
 *      - manualni vytvoreni, bude brat Course entity
 * /api/courses/kos
 * - POST:
 *     - import z kosu
 * /api/courses/id
 * /api/courses/id/semesterCode
 * - POST:
 *    - vytvor instanci kurzu v semestru na zaklade course
 * /api/courses/id/semesterCode/participants
 * - POST:
 *     - import z KOSu
 * /api/courses/id/semesterCode/participants/students
 * - POST:
 *      - enrol
 * - DELETE:
 *      - unenrol
 * /api/courses/id/semesterCode/participants/teachers
 * - POST:
 *      - enrol
 * - DELETE:
 *      - unenrol
 * /api/courses/id/semesterCode/parallels
 * - POST:
 *      - enrol
 *      - create parallel
 * - DELETE:
 *      - unenrol
 *      - remove parallel
 * /api/courses/id/parallels/participants?sem=default_newest TODO: mozna?
 **/
@RestController
@RequestMapping("/api/courses")
// TODO security
public class CourseController {

    private static final Logger LOG = LoggerFactory.getLogger(CourseController.class);

    private final CourseService courseService;

    private final CourseInSemesterService courseInSemesterService;

    private final SemesterService semesterService;

    private final ParallelService parallelService;

    private final KosapiService kosapiService;

    public CourseController(CourseService courseService, CourseInSemesterService courseInSemesterService,
                            SemesterService semesterService, ParallelService parallelService, KosapiService kosapiService) {
        this.courseService = courseService;
        this.courseInSemesterService = courseInSemesterService;
        this.semesterService = semesterService;
        this.parallelService = parallelService;
        this.kosapiService = kosapiService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Course> getCourses() {
        return courseService.findAll();
    }

    @GetMapping(value = "/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    public Course getCourseById(@PathVariable Integer id) {
        return courseService.find(id);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createCourse(@RequestBody Course course) {
        courseService.persist(course);
        LOG.debug("Created course {}.", course);
        final HttpHeaders headers = RestUtils.createLocationHeaderFromCurrentUri("/{id}", course.getId());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @PostMapping(value = "/kos", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createCourseFromKos(@RequestBody String code) {
        KosCourse kosCourse = kosapiService.getCourse(code);
        Course course = courseService.createNewCourse(kosCourse);
        LOG.debug("Created course {} \n from kos course {}.", course, kosCourse);
        final HttpHeaders headers = RestUtils.createLocationHeaderFromCurrentUri("/{id}", course.getId());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }
}

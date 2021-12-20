package cz.cvut.kbss.ear.project.rest;

import cz.cvut.kbss.ear.project.model.Course;
import cz.cvut.kbss.ear.project.model.Semester;
import cz.cvut.kbss.ear.project.rest.util.RestUtils;
import cz.cvut.kbss.ear.project.service.SemesterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Resources
 * /api/semester
 * Post: code
 *
 * /api/semester/semestercode
 * /api/semester/semestercode/makecurrent
 */

@RestController
@RequestMapping("/api/semester")
// TODO security
public class SemesterController {
    private static final Logger LOG = LoggerFactory.getLogger(SemesterController.class);

    private final SemesterService semesterService;

    public SemesterController(SemesterService semesterService) {
        this.semesterService = semesterService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Semester> getSemesters() {
        return semesterService.findAll();
    }

    @GetMapping(value = "/{semesterCode}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Semester getSemester(@PathVariable String semesterCode){
        return semesterService.findByCode(semesterCode);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createSemester(@RequestBody Semester semester) {
        semesterService.addNewSemester(semester.getCode(), semester.getYear(), semester.getType());
        LOG.debug("Created semester {}. Its state was set to preparation.", semester);
        final HttpHeaders headers = RestUtils.createLocationHeaderFromCurrentUri("/{semesterCode}", semester.getCode());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @PostMapping(value = "/{semesterCode}/makecurrent", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> makeCurrent(@PathVariable String semesterCode) {
        semesterService.makeSemesterCurrent(semesterService.findByCode(semesterCode));
        LOG.debug("Made semester with code: {}, current.", semesterCode);
        final HttpHeaders headers = RestUtils.createLocationHeaderFromCurrentUri(""); // Todo wrong uri
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }
}

package cz.cvut.kbss.ear.project.rest.controllers;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import cz.cvut.kbss.ear.project.exception.InvalidInputDataException;
import cz.cvut.kbss.ear.project.exception.NotFoundException;
import cz.cvut.kbss.ear.project.model.Semester;
import cz.cvut.kbss.ear.project.model.User;
import cz.cvut.kbss.ear.project.model.enums.Role;
import cz.cvut.kbss.ear.project.rest.dto.MeDTO;
import cz.cvut.kbss.ear.project.rest.dto.TimetableSlotDTO;
import cz.cvut.kbss.ear.project.rest.util.RestUtils;
import cz.cvut.kbss.ear.project.service.ParallelService;
import cz.cvut.kbss.ear.project.service.SemesterService;
import cz.cvut.kbss.ear.project.service.UserService;
import java.io.IOException;
import java.security.Principal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.minidev.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    private final SemesterService semesterService;

    private final ParallelService parallelService;

    @Autowired
    public UserController(UserService userService, SemesterService semesterService, ParallelService parallelService) {
        this.userService = userService;
        this.semesterService = semesterService;
        this.parallelService = parallelService;
    }

    /**
     * Registers a new user.
     *
     * @param user User data
     */
    @PreAuthorize("(!#user.isAdmin() && anonymous) || hasRole('ROLE_ADMIN')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> register(@RequestBody User user) { // TODO: Create User Registration DTO
        userService.persist(user);
        LOG.debug("User {} successfully registered.", user);
        final HttpHeaders headers = RestUtils.createLocationHeaderFromCurrentUri("/current");
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping(value = "/me", produces = MediaType.APPLICATION_JSON_VALUE)
    public MeDTO getCurrent(Principal principal) {
        final UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) principal;
        LOG.debug(auth.getAuthorities().toString());
        User user = userService.getUserByUsername((String) auth.getPrincipal());
        if (user == null) {
            throw new NotFoundException("User does not exist.");
        }
        return new MeDTO(user);
    }

    /**
     * Resources
     *
     * api/users/username/semester/parallels
     * api/users/username/role
     * - PUT:
     *      - urcit roli
     *
     */
    @GetMapping(value = "/{username}/{semesterCode}/timetable", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<TimetableSlotDTO> getUsersParallels(@PathVariable String username, @PathVariable String semesterCode) {
        User user = userService.findByUsername(username);
        Semester semester = semesterService.findByCode(semesterCode);
        if (semester == null) throw NotFoundException.create("Semester", semesterCode);
        if (user == null) throw NotFoundException.create("User", username);
        return parallelService.getUsersParallelsInSemester(user, semester)
                .stream()
                .map(TimetableSlotDTO::new)
                .collect(Collectors.toList());
    }

    @PutMapping(value = "/{username}/role", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void setRole(@PathVariable String username, @RequestBody Map<String, Object> inputData) {
        User user = userService.findByUsername(username);
        if (user == null) {
            throw NotFoundException.create("User", username);
        }

        JSONObject jsonObj = new JSONObject(inputData); // I am doing it this way, because i was unable to deserialize enum Role directly
        String rolename = jsonObj.getAsString("role");
        try{
            Role role = Role.valueOf(rolename);
            userService.setUserRole(user, role);
        }
        catch(IllegalArgumentException e){
            throw new InvalidInputDataException("Invalid input for role.");
        }
    }
}

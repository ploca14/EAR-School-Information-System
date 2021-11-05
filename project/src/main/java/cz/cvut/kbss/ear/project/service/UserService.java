package cz.cvut.kbss.ear.project.service;

import cz.cvut.kbss.ear.project.dao.CourseParticipantDao;
import cz.cvut.kbss.ear.project.dao.UserDao;
import cz.cvut.kbss.ear.project.exception.UserException;
import cz.cvut.kbss.ear.project.model.Parallel;
import cz.cvut.kbss.ear.project.model.User;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserDao dao;
    private final CourseParticipantDao courseParticipantDao;
    private final PasswordEncoder passwordEncoder;

    public UserService(
        UserDao userDao, CourseParticipantDao courseParticipantDao, PasswordEncoder passwordEncoder
    ) {
        this.dao = userDao;
        this.courseParticipantDao = courseParticipantDao;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public List<User> findAll() {
        return dao.findAll();
    }

    @Transactional
    public User find(Integer id) {
        return dao.find(id);
    }

    @Transactional
    public void persist(User user) {
        Objects.requireNonNull(user);

        if (!isUsernameUnique(user.getUsername())) {
            throw new UserException(
                String.format("User with username %s already exists", user.getUsername()));
        }

        user.encodePassword(passwordEncoder);

        dao.persist(user);
    }

    @Transactional
    public Collection<Parallel> getTimetableInSemester(User user) {
        return courseParticipantDao.findAllByUser(user).stream()
            .flatMap(u -> u.getParallels().stream()).collect(
                Collectors.toSet());
    }

    private boolean isUsernameUnique(String username) {
        return dao.findByUsername(username) == null;
    }
}

package cz.cvut.kbss.ear.project.service;

import cz.cvut.kbss.ear.project.dao.CourseParticipantDao;
import cz.cvut.kbss.ear.project.dao.UserDao;
import cz.cvut.kbss.ear.project.exception.UserException;
import cz.cvut.kbss.ear.project.model.Parallel;
import cz.cvut.kbss.ear.project.model.User;
import java.util.Collection;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserDao dao;
    private final CourseParticipantDao courseParticipantDao;

    public UserService(UserDao userDao, CourseParticipantDao courseParticipantDao) {
        this.dao = userDao;
        this.courseParticipantDao = courseParticipantDao;
    }

    @Transactional
    public User persist(User user) {
        if (!isUsernameUnique(user.getUsername())) {
            throw new UserException(
                String.format("User with username %s already exists", user.getUsername()));
        }

        dao.persist(user);
        return user;
    }

    public Collection<Parallel> getTimetableInSemester(User user) {
        return courseParticipantDao.findAllByUser(user).stream()
            .flatMap(u -> u.getParallels().stream()).collect(
                Collectors.toSet());
    }

    private boolean isUsernameUnique(String username) {
        return dao.findByUsername(username) == null;
    }
}

package cz.cvut.kbss.ear.project.dao;

import cz.cvut.kbss.ear.project.model.User;
import org.springframework.stereotype.Repository;

@Repository
public class UserDao extends BaseDao<User>{
    public UserDao() {
        super(User.class);
    }
}

package pro.kuli4.otus.java.hw09.core.dao;

import pro.kuli4.otus.java.hw09.core.model.User;
import pro.kuli4.otus.java.hw09.core.sessionmanager.SessionManager;

import java.util.Optional;


public interface UserDao {
    Optional<User> findById(long id);

    long insertOrUpdate(User user);

    SessionManager getSessionManager();
}

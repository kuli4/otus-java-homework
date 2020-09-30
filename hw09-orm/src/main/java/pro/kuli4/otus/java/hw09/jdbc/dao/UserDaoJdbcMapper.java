package pro.kuli4.otus.java.hw09.jdbc.dao;

import pro.kuli4.otus.java.hw09.core.dao.UserDao;
import pro.kuli4.otus.java.hw09.core.model.User;
import pro.kuli4.otus.java.hw09.core.sessionmanager.SessionManager;
import pro.kuli4.otus.java.hw09.jdbc.mapper.JdbcMapper;

import java.util.Optional;

public class UserDaoJdbcMapper implements UserDao {

    private final SessionManager sessionManager;
    private final JdbcMapper<User> jdbcMapper;

    public UserDaoJdbcMapper(SessionManager sessionManager, JdbcMapper<User> jdbcMapper) {
        this.sessionManager = sessionManager;
        this.jdbcMapper = jdbcMapper;
    }

    @Override
    public Optional<User> findById(long id) {
        return jdbcMapper.findById(id, User.class);
    }

    @Override
    public long insertOrUpdate(User user) {
        return (long) jdbcMapper.insertOrUpdate(user);
    }

    @Override
    public SessionManager getSessionManager() {
        return sessionManager;
    }
}

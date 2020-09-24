package pro.kuli4.otus.java.hw09.jdbc.dao;

import pro.kuli4.otus.java.hw09.core.dao.AccountDao;
import pro.kuli4.otus.java.hw09.core.model.Account;
import pro.kuli4.otus.java.hw09.core.sessionmanager.SessionManager;
import pro.kuli4.otus.java.hw09.jdbc.mapper.JdbcMapper;

import java.util.Optional;

public class AccountDaoJdbcMapper implements AccountDao {

    private final SessionManager sessionManager;
    private final JdbcMapper<Account> jdbcMapper;

    public AccountDaoJdbcMapper(SessionManager sessionManager, JdbcMapper<Account> jdbcMapper) {
        this.sessionManager = sessionManager;
        this.jdbcMapper = jdbcMapper;
    }

    @Override
    public Optional<Account> findById(long id) {
        return jdbcMapper.findById(id, Account.class);
    }

    @Override
    public long insertOrUpdate(Account account) {
        return (long) jdbcMapper.insertOrUpdate(account);
    }

    @Override
    public SessionManager getSessionManager() {
        return sessionManager;
    }
}

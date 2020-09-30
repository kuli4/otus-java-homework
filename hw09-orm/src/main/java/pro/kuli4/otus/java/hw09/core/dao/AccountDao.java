package pro.kuli4.otus.java.hw09.core.dao;

import pro.kuli4.otus.java.hw09.core.model.Account;
import pro.kuli4.otus.java.hw09.core.sessionmanager.SessionManager;

import java.util.Optional;


public interface AccountDao {
    Optional<Account> findById(long id);

    long insertOrUpdate(Account account);

    SessionManager getSessionManager();
}

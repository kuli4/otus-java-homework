package pro.kuli4.otus.java.hw09.core.service;

import lombok.extern.slf4j.Slf4j;
import pro.kuli4.otus.java.hw09.core.dao.AccountDao;
import pro.kuli4.otus.java.hw09.core.model.Account;

import java.util.Optional;

@Slf4j
public class DbServiceAccountImpl implements DbServiceAccount {

    private final AccountDao accountDao;

    public DbServiceAccountImpl(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    @Override
    public long saveAccount(Account account) {
        try (var sessionManager = accountDao.getSessionManager()) {
            sessionManager.beginSession();
            try {
                var accountId = accountDao.insertOrUpdate(account);
                sessionManager.commitSession();
                return accountId;
            } catch (Exception e) {
                sessionManager.rollbackSession();
                throw new DbServiceException(e);
            }
        }
    }

    @Override
    public Optional<Account> getAccount(long id) {
        try (var sessionManager = accountDao.getSessionManager()) {
            sessionManager.beginSession();
            try {
                return accountDao.findById(id);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                sessionManager.rollbackSession();
            }
            return Optional.empty();
        }
    }
}

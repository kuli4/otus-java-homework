package pro.kuli4.otus.java.hw09.core.service;

import pro.kuli4.otus.java.hw09.core.model.Account;

import java.util.Optional;

public interface DbServiceAccount {

    long saveAccount(Account account);

    Optional<Account> getAccount(long id);
}

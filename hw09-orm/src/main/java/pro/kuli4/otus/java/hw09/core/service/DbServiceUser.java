package pro.kuli4.otus.java.hw09.core.service;

import pro.kuli4.otus.java.hw09.core.model.User;
import java.util.Optional;

public interface DbServiceUser {

    long saveUser(User user);

    Optional<User> getUser(long id);
}

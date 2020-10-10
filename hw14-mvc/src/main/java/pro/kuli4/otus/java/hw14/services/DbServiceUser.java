package pro.kuli4.otus.java.hw14.services;

import pro.kuli4.otus.java.hw14.entities.User;

import java.util.Optional;

public interface DbServiceUser {

    long saveUser(User user);

    Optional<User> getUser(long id);

}

package pro.kuli4.otus.java.hw10.services;

import pro.kuli4.otus.java.hw10.entities.User;

import java.util.Optional;

public interface DbServiceUser {

    long saveUser(User user);

    Optional<User> getUser(long id);

}

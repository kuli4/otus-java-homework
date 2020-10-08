package pro.kuli4.otus.java.hw12.service;



import pro.kuli4.otus.java.hw12.entities.User;

import java.util.Optional;

public interface DbServiceUser {

    long saveUser(User user);

    Optional<User> getUser(long id);

}

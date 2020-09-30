package pro.kuli4.otus.java.hw10.dao;


import pro.kuli4.otus.java.hw10.entities.User;

import java.util.Optional;

public interface UserDao {
    Optional<User> findById(long id);

    long insertUser(User user);

    void updateUser(User user);

    void insertOrUpdate(User user);

}

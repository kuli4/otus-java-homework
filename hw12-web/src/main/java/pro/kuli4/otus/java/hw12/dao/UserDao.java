package pro.kuli4.otus.java.hw12.dao;


import pro.kuli4.otus.java.hw12.entities.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {
    Optional<User> findById(long id);

    public Optional<User> findByLogin(String login);

    long insertUser(User user);

    void updateUser(User user);

    void insertOrUpdate(User user);

    List<User> getAllUsers();
}

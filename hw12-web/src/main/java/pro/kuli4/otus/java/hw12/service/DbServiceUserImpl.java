package pro.kuli4.otus.java.hw12.service;

import lombok.extern.slf4j.Slf4j;
import pro.kuli4.otus.java.hw12.dao.UserDao;
import pro.kuli4.otus.java.hw12.entities.User;

import java.util.Optional;

@Slf4j
public class DbServiceUserImpl implements DbServiceUser {
    private final UserDao userDao;

    public DbServiceUserImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public long saveUser(User user) {
        userDao.insertOrUpdate(user);
        return user.getId();
    }


    @Override
    public Optional<User> getUser(long id) {
        return userDao.findById(id);
    }
}
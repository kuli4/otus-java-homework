package pro.kuli4.otus.java.hw14.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pro.kuli4.otus.java.hw14.dao.UserDao;
import pro.kuli4.otus.java.hw14.entities.User;

import java.util.Optional;

@Slf4j
@Service
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
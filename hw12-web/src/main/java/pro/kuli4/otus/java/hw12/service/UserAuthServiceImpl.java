package pro.kuli4.otus.java.hw12.service;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import pro.kuli4.otus.java.hw12.dao.UserDao;
import pro.kuli4.otus.java.hw12.entities.User;

import java.util.Optional;

@Slf4j
public class UserAuthServiceImpl implements UserAuthService {

    private final UserDao userDao;

    public UserAuthServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public boolean authenticate(String login, String password) {
        Optional<User> userOptional = userDao.findByLogin(login);
        log.info("User is present: {}", userOptional.isPresent());
        return userOptional.isPresent() && userOptional.get().getPassword().equals(DigestUtils.md5Hex(password));
    }
}

package pro.kuli4.otus.java.hw11.service;

import lombok.extern.slf4j.Slf4j;
import pro.kuli4.otus.java.hw10.services.DbServiceUserImpl;
import pro.kuli4.otus.java.hw10.dao.UserDao;
import pro.kuli4.otus.java.hw10.entities.User;
import pro.kuli4.otus.java.hw11.cache.Cache;

import java.util.Optional;

@Slf4j
public class DbServiceUserWithCache extends DbServiceUserImpl {

    private final Cache<String, User> cache;

    public DbServiceUserWithCache(UserDao userDao, Cache<String, User> cache) {
        super(userDao);
        this.cache = cache;
    }

    @Override
    public Optional<User> getUser(long id) {
        User cacheUser = cache.get(constructKey(id));
        if (cacheUser == null) {
            Optional<User> userOptional = super.getUser(id);
            userOptional.ifPresent((user) -> cache.put(constructKey(user.getId()), user));
            return userOptional;
        }
        return Optional.of(cacheUser);
    }

    @Override
    public long saveUser(User user) {
        long id = super.saveUser(user);
        cache.put(constructKey(id), user);
        return id;
    }

    private String constructKey(long id) {
        return "k" + id;
    }
}

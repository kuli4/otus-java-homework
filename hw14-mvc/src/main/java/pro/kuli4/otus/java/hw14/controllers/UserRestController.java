package pro.kuli4.otus.java.hw14.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import pro.kuli4.otus.java.hw14.dao.UserDao;
import pro.kuli4.otus.java.hw14.dto.UserDto;
import pro.kuli4.otus.java.hw14.dto.UserDtoMapper;
import pro.kuli4.otus.java.hw14.entities.PhoneDataSet;
import pro.kuli4.otus.java.hw14.entities.User;
import pro.kuli4.otus.java.hw14.services.DbServiceUser;
import pro.kuli4.otus.java.hw14.services.PassEncoder;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
public class UserRestController {

    private final UserDao userDao;
    private final UserDtoMapper userDtoMapper;
    private final DbServiceUser dbServiceUser;
    private final PassEncoder passEncoder;

    public UserRestController(UserDao userDao, UserDtoMapper userDtoMapper, DbServiceUser dbServiceUser, PassEncoder passEncoder) {
        this.userDao = userDao;
        this.userDtoMapper = userDtoMapper;
        this.dbServiceUser = dbServiceUser;
        this.passEncoder = passEncoder;
    }

    @GetMapping("/api/user")
    public List<UserDto> getAllUsers() {
        return userDao.getAllUsers().stream().map(userDtoMapper::map).collect(Collectors.toList());
    }

    @PostMapping("/api/user")
    public void saveUser(@RequestBody User user) {
        try {
            for (PhoneDataSet phoneDataSet : user.getPhones()) {
                phoneDataSet.setUser(user);
            }
            user.setPassword(passEncoder.encode(user.getPassword()));
            dbServiceUser.saveUser(user);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "problems");
        }
    }
}

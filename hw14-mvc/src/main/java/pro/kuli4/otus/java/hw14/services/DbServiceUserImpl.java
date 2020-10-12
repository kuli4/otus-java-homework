package pro.kuli4.otus.java.hw14.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pro.kuli4.otus.java.hw14.dao.UserDao;
import pro.kuli4.otus.java.hw14.entities.AddressDataSet;
import pro.kuli4.otus.java.hw14.entities.PhoneDataSet;
import pro.kuli4.otus.java.hw14.entities.User;

import javax.annotation.PostConstruct;
import java.util.Optional;

@Slf4j
@Service
public class DbServiceUserImpl implements DbServiceUser {
    private final UserDao userDao;
    private final PassEncoder passEncoder;

    public DbServiceUserImpl(UserDao userDao, PassEncoder passEncoder) {
        this.userDao = userDao;
        this.passEncoder = passEncoder;
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

    @PostConstruct
    void initDb() {
        createUser(this, "Sergey", "serg", passEncoder.encode("1"), "Svetlaya");
        createUser(this, "Alexander", "alex", passEncoder.encode("12"), "Lenina");
        createUser(this, "Pavel", "pavel", passEncoder.encode("123"), "Tverskaya");
        createUser(this, "Ivan", "ivan", passEncoder.encode("1234"), "Gagarina");
    }

    private void createUser(DbServiceUser dbServiceUser, String name, String login, String password, String street) {
        // Create user
        User user = new User();
        user.setName(name);
        user.setLogin(login);
        user.setPassword(password);

        // Create address
        AddressDataSet address = new AddressDataSet();
        address.setStreet(street + " street");
        user.setAddress(address);

        // Create two phones
        PhoneDataSet mobilePhone = new PhoneDataSet();
        mobilePhone.setNumber(phoneNumberGen());
        user.getPhones().add(mobilePhone);
        mobilePhone.setUser(user);

        PhoneDataSet homePhone = new PhoneDataSet();
        homePhone.setNumber(phoneNumberGen());
        user.getPhones().add(homePhone);
        homePhone.setUser(user);

        log.debug("application user: {}", user);
        dbServiceUser.saveUser(user);
    }

    private String phoneNumberGen() {
        StringBuilder sb = new StringBuilder("+7");
        for (int i = 0; i < 10; i++) {
            sb.append(Math.round((Math.random() * 100) % 9));
        }
        return sb.toString();
    }

}
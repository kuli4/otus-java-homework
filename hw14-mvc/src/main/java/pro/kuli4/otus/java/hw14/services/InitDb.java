package pro.kuli4.otus.java.hw14.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import pro.kuli4.otus.java.hw14.entities.AddressDataSet;
import pro.kuli4.otus.java.hw14.entities.PhoneDataSet;
import pro.kuli4.otus.java.hw14.entities.User;

@Component
@Slf4j
public class InitDb {

    public InitDb(DbServiceUser dbServiceUser, PassEncoder passEncoder) {
        createUser(dbServiceUser, "Sergey", "serg", passEncoder.encode("1"), "Svetlaya");
        createUser(dbServiceUser, "Alexander", "alex", passEncoder.encode("12"), "Lenina");
        createUser(dbServiceUser, "Pavel", "pavel", passEncoder.encode("123"), "Tverskaya");
        createUser(dbServiceUser, "Ivan", "ivan", passEncoder.encode("1234"), "Gagarina");
    }

    private static void createUser(DbServiceUser dbServiceUser, String name, String login, String password, String street) {
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

    private static String phoneNumberGen() {
        StringBuilder sb = new StringBuilder("+7");
        for (int i = 0; i < 10; i++) {
            sb.append(Math.round((Math.random() * 100) % 9));
        }
        return sb.toString();
    }

}

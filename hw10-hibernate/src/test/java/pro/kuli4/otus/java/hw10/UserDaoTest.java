package pro.kuli4.otus.java.hw10;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pro.kuli4.otus.java.hw10.dao.UserDaoHibernate;
import pro.kuli4.otus.java.hw10.dao.UserDao;
import pro.kuli4.otus.java.hw10.entities.User;
import pro.kuli4.otus.java.hw10.entities.AddressDataSet;
import pro.kuli4.otus.java.hw10.entities.PhoneDataSet;

import static org.assertj.core.api.Assertions.*;

import java.util.Optional;

@DisplayName("Dao для работы с пользователями должно ")
public class UserDaoTest {
    public static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";
    public static SessionFactory sessionFactory;
    public static UserDao userDao;

    @BeforeAll
    public static void setUp() {
        Configuration cfg = new Configuration().configure(HIBERNATE_CFG_FILE);

        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                .applySettings(cfg.getProperties()).build();

        Metadata metadata = new MetadataSources(serviceRegistry)
                .addAnnotatedClass(User.class)
                .addAnnotatedClass(AddressDataSet.class)
                .addAnnotatedClass(PhoneDataSet.class)
                .getMetadataBuilder()
                .build();

        sessionFactory = metadata.getSessionFactoryBuilder().build();
        userDao = new UserDaoHibernate(sessionFactory);
    }

    @DisplayName("корректно получать пользователя по id")
    @Test
    public void shouldCorrectFindUserById() {
        User originalUser = createUser();
        try(Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(originalUser);
            assertThat(originalUser.getId()).isGreaterThan(0);
            session.getTransaction().commit();

            Optional<User> dbUserOptional = userDao.findById(originalUser.getId());

            assertThat(dbUserOptional).isPresent();
            assertThat(dbUserOptional.get().toString()).isEqualTo(originalUser.toString());
        }

    }

    @DisplayName("корректно добавлять пользователя в БД")
    @Test
    public void shouldCorrectInsertUser() {
        User originalUser = createUser();
        Long id = userDao.insertUser(originalUser);

        Optional<User> dbUserOptional = userDao.findById(originalUser.getId());
        assertThat(dbUserOptional).isPresent();
        assertThat(dbUserOptional.get().toString()).isEqualTo(originalUser.toString());
    }

    @DisplayName("корректно обновлять пользователя в БД")
    @Test
    public void shouldCorrectUpdateUser() {
        User originalUser = createUser();
        userDao.insertUser(originalUser);

        Optional<User> dbUserOptional = userDao.findById(originalUser.getId());
        assertThat(dbUserOptional).isPresent();
        User dbUser = dbUserOptional.get();
        dbUser.setName("Not Harry");
        userDao.updateUser(dbUser);

        Optional<User> dbUserOptionalToo = userDao.findById(dbUser.getId());

        assertThat(dbUserOptionalToo.get().getName()).isEqualTo("Not Harry");
    }

    private User createUser() {
        User user = new User();
        user.setName("Harry");

        // Create address
        AddressDataSet address = new AddressDataSet();
        address.setStreet("Privet street");
        user.setAddress(address);

        // Create two phones
        PhoneDataSet mobilePhone = new PhoneDataSet();
        mobilePhone.setNumber("+441");
        user.getPhones().add(mobilePhone);
        mobilePhone.setUser(user);

        PhoneDataSet homePhone = new PhoneDataSet();
        homePhone.setNumber("+442");
        user.getPhones().add(homePhone);
        homePhone.setUser(user);
        return user;
    }

    @AfterAll
    public static void tearDown() {
        sessionFactory.close();
    }

}

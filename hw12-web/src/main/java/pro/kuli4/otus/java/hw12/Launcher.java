package pro.kuli4.otus.java.hw12;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import pro.kuli4.otus.java.hw12.dao.UserDao;
import pro.kuli4.otus.java.hw12.dao.UserDaoHibernate;
import pro.kuli4.otus.java.hw12.entities.AddressDataSet;
import pro.kuli4.otus.java.hw12.entities.PhoneDataSet;
import pro.kuli4.otus.java.hw12.entities.User;
import pro.kuli4.otus.java.hw12.server.UsersWebServer;
import pro.kuli4.otus.java.hw12.server.UsersWebServerSimple;
import pro.kuli4.otus.java.hw12.service.*;


/*
 * For start:
 * ./gradlew hw12-web:clean hw12-web:build
 * java -jar hw12-web/build/libs/hw12-web-0.0.1.jar
 *
 *
 * http://localhost:8080/ - start login page
 * http://localhost:8080/private/ - admin page
 * http://localhost:8080/api/user/ [GET] - get all users list
 * http://localhost:8080/api/user/ [POST] - add new user
 */

@Slf4j
public class Launcher {
    private static final int WEB_SERVER_PORT = 8080;
    private static final String TEMPLATES_DIR = "/templates/";
    private static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";

    public static void main(String[] args) throws Exception {
        SessionFactory sessionFactory = initHibernate();
        Gson gson = initGson();

        //Create DAO, DTO and DB service
        UserDao userDao = new UserDaoHibernate(sessionFactory);
        DbServiceUser dbServiceUser = new DbServiceUserImpl(userDao);

        //Create auxiliary services
        TemplateProcessor templateProcessor = new TemplateProcessorImpl(TEMPLATES_DIR);
        PassEncoder passEncoder = new UserPassEncoder();
        UserAuthService authService = new UserAuthServiceImpl(userDao, passEncoder);

        //Initialize server
        UsersWebServer usersWebServer = new UsersWebServerSimple(
                WEB_SERVER_PORT,
                authService,
                userDao,
                templateProcessor,
                gson,
                passEncoder
        );

        createUser(dbServiceUser, "Sergey", "serg", passEncoder.encode("1"), "Svetlaya");
        createUser(dbServiceUser, "Alexander", "alex", passEncoder.encode("12"), "Lenina");
        createUser(dbServiceUser, "Pavel", "pavel", passEncoder.encode("123"), "Tverskaya");
        createUser(dbServiceUser, "Ivan", "ivan", passEncoder.encode("1234"), "Gagarina");

        //Start server
        usersWebServer.start();

        //Keep application in action
        usersWebServer.join();
    }

    private static SessionFactory initHibernate() {
        Configuration cfg = new Configuration().configure(HIBERNATE_CFG_FILE);

        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                .applySettings(cfg.getProperties()).build();

        Metadata metadata = new MetadataSources(serviceRegistry)
                .addAnnotatedClass(User.class)
                .addAnnotatedClass(AddressDataSet.class)
                .addAnnotatedClass(PhoneDataSet.class)
                .getMetadataBuilder()
                .build();

        return metadata.getSessionFactoryBuilder().build();
    }

    private static Gson initGson() {
        ExclusionStrategy strategy = new ExclusionStrategy() {
            @Override
            public boolean shouldSkipField(FieldAttributes field) {
                if (field.getName().equals("user") || field.getName().equals("password") || field.getName().equals("id")) {
                    return true;
                }
                return false;
            }

            @Override
            public boolean shouldSkipClass(Class<?> clazz) {
                return false;
            }
        };
        return new GsonBuilder()
                .serializeNulls()
                .setPrettyPrinting()
                .addSerializationExclusionStrategy(strategy)
                .create();
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

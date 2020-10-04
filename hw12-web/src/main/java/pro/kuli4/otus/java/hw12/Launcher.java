package pro.kuli4.otus.java.hw12;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import pro.kuli4.otus.java.hw12.dao.UserDao;
import pro.kuli4.otus.java.hw12.dao.UserDaoHibernate;
import pro.kuli4.otus.java.hw12.dto.UserJsonDto;
import pro.kuli4.otus.java.hw12.dto.UserJsonDtoHibernate;
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
        // Initialize hibernate and DB
        Configuration cfg = new Configuration().configure(HIBERNATE_CFG_FILE);

        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                .applySettings(cfg.getProperties()).build();

        Metadata metadata = new MetadataSources(serviceRegistry)
                .addAnnotatedClass(User.class)
                .addAnnotatedClass(AddressDataSet.class)
                .addAnnotatedClass(PhoneDataSet.class)
                .getMetadataBuilder()
                .build();

        SessionFactory sessionFactory = metadata.getSessionFactoryBuilder().build();

        // Build gson
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
        Gson gson = new GsonBuilder()
                .serializeNulls()
                .setPrettyPrinting()
                .addSerializationExclusionStrategy(strategy)
                .create();

        //Create DAO, DTO and DB service
        UserDao userDao = new UserDaoHibernate(sessionFactory);
        UserJsonDto userJsonDto = new UserJsonDtoHibernate(sessionFactory, gson);
        DbServiceUser dbServiceUser = new DbServiceUserImpl(userDao);

        createUser(dbServiceUser, "Sergey", "serg", DigestUtils.md5Hex("1"), "Svetlaya");
        createUser(dbServiceUser, "Alexander", "alex", DigestUtils.md5Hex("12"), "Lenina");
        createUser(dbServiceUser, "Pavel", "pavel", DigestUtils.md5Hex("123"), "Tverskaya");
        createUser(dbServiceUser, "Ivan", "ivan", DigestUtils.md5Hex("1234"), "Gagarina");

        //Create auxiliary services
        TemplateProcessor templateProcessor = new TemplateProcessorImpl(TEMPLATES_DIR);
        UserAuthService authService = new UserAuthServiceImpl(userDao);

        //Initialize server
        UsersWebServer usersWebServer = new UsersWebServerSimple(
                WEB_SERVER_PORT,
                authService,
                userDao,
                userJsonDto,
                templateProcessor,
                gson
        );

        //Start server
        usersWebServer.start();

        //Keep application in action
        usersWebServer.join();
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

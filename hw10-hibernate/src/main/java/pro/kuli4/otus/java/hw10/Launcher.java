package pro.kuli4.otus.java.hw10;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import pro.kuli4.otus.java.hw10.dao.UserDao;
import pro.kuli4.otus.java.hw10.dao.UserDaoHibernate;
import pro.kuli4.otus.java.hw10.entities.AddressDataSet;
import pro.kuli4.otus.java.hw10.entities.PhoneDataSet;
import pro.kuli4.otus.java.hw10.entities.User;
import pro.kuli4.otus.java.hw10.services.DbServiceUser;
import pro.kuli4.otus.java.hw10.services.DbServiceUserImpl;

import java.sql.ResultSetMetaData;
import java.util.Optional;


@Slf4j
public class Launcher {
    public static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";

    private final DbServiceUser dbServiceUser;
    private final SessionFactory sessionFactory;

    public static void main(String[] args) {
        Launcher launcher = new Launcher();
        long id = launcher.createUser();
        launcher.printTable("USERS");
        launcher.printTable("ADDRESSES");
        launcher.printTable("PHONES");
        launcher.readUser(id);
    }

    private Launcher() {
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
        UserDao userDao = new UserDaoHibernate(sessionFactory);
        dbServiceUser = new DbServiceUserImpl(userDao);
    }

    private Long createUser() {
            // Create user
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

            log.debug("application user: {}", user);

            return dbServiceUser.saveUser(user);
    }

    private void readUser(Long id) {
        Optional<User> optionalUser = dbServiceUser.getUser(id);
        if(optionalUser.isPresent()) {
            User dbUser = optionalUser.get();
            log.info("User from db: {}", dbUser);
        } else {
            log.info("No user!");
        }
    }

    private void printTable(String tableName) {
        try (var session = sessionFactory.openSession()) {
            System.out.println("===========" + tableName + "============");
            session.doWork(connection -> {
                try (var ps = connection.prepareStatement("select * from " + tableName)) {
                    try (var rs = ps.executeQuery()) {
                        ResultSetMetaData rsmd = rs.getMetaData();
                        int columnsNumber = rsmd.getColumnCount();
                        while (rs.next()) {
                            for (int i = 1; i <= columnsNumber; i++) {
                                if (i > 1) System.out.print(",  ");
                                String columnValue = rs.getString(i);
                                System.out.print(rsmd.getColumnName(i) + " = " + columnValue);
                            }
                            System.out.println("");
                        }
                    }
                }
            });
            System.out.println();
        }
    }
}

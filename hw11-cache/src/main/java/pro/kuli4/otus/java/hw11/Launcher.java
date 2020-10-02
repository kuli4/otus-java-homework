package pro.kuli4.otus.java.hw11;

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
import pro.kuli4.otus.java.hw11.cache.Cache;
import pro.kuli4.otus.java.hw11.cache.CacheListener;
import pro.kuli4.otus.java.hw11.cache.MyCache;
import pro.kuli4.otus.java.hw11.cache.PrintActionCacheListener;
import pro.kuli4.otus.java.hw11.service.DbServiceUserWithCache;

/*
* For start:
* ./gradlew hw11-cache:clean hw11-cache:build
* java -Xmx128m -Xms128m -Xlog:gc -jar hw11-cache/build/libs/hw11-cache-0.0.1.jar
 */

@Slf4j
public class Launcher {
    public static final String HIBERNATE_CFG_FILE = "hibernate.cfg-hw11.xml";

    public static void main(String[] args) {
        Cache<String,User> cache = new MyCache<>();
        CacheListener<String, User> cacheListener = new PrintActionCacheListener<>();
        CacheListener<String, User> cacheListener2 = new PrintActionCacheListener<>();
        cache.addListener(cacheListener);
        cache.addListener(cacheListener2);
        cache.removeListener(cacheListener2);

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
        UserDao userDao = new UserDaoHibernate(sessionFactory);
        DbServiceUser dbServiceUserWithCache = new DbServiceUserWithCache(userDao, cache);
        DbServiceUser dbServiceUser = new DbServiceUserImpl(userDao);
        log.info("Cache size: {}", cache.getSize());
        for(int i = 0; i < 100; i++) {
            User user = createUser();
            user.setName(user.getName() + i);
            dbServiceUserWithCache.saveUser(user);
        }
        log.info("Cache size: {}", cache.getSize());
        log.info("=========================================");
        long startTime = System.currentTimeMillis();
        log.info("Begin with cache");
        log.info("=========================================");
        for(int i = 0; i <100; i++) {
            dbServiceUserWithCache.getUser(i+1);
        }
        log.info("=========================================");
        log.info("End with cache. Total time: {}", System.currentTimeMillis()-startTime);
        log.info("=========================================");
        log.info("Cache size: {}", cache.getSize());
        startTime = System.currentTimeMillis();
        log.info("=========================================");
        log.info("Begin without cache");
        log.info("=========================================");
        for(int i = 0; i <100; i++) {
            dbServiceUser.getUser(i+1);
        }
        log.info("=========================================");
        log.info("End without cache. Total time: {}", System.currentTimeMillis()-startTime);
        log.info("=========================================");

        log.info("=========================================");
        log.info("Check cache cleaning");
        log.info("=========================================");
        log.info("Cache size: {}", cache.getSize());
        for(int i = 100; i < 10000; i++) {
            User user = createUser();
            user.setName(user.getName() + i);
            dbServiceUserWithCache.saveUser(user);
        }
        for(int i = 0; i <10000; i++) {
            dbServiceUserWithCache.getUser(i+1);
            if(i % 100 == 0) {
                log.info("=========================================");
                log.info("Cache size: {}", cache.getSize());
            }
        }

    }

    public static User createUser() {
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
        return user;
    }

}

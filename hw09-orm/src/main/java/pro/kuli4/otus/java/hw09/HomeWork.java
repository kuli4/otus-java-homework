package pro.kuli4.otus.java.hw09;

import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;
import pro.kuli4.otus.java.hw09.core.dao.AccountDao;
import pro.kuli4.otus.java.hw09.core.dao.UserDao;
import pro.kuli4.otus.java.hw09.core.model.Account;
import pro.kuli4.otus.java.hw09.core.model.User;
import pro.kuli4.otus.java.hw09.core.service.DbServiceAccountImpl;
import pro.kuli4.otus.java.hw09.core.service.DbServiceUserImpl;
import pro.kuli4.otus.java.hw09.h2.DataSourceH2;
import pro.kuli4.otus.java.hw09.jdbc.DbExecutorImpl;
import pro.kuli4.otus.java.hw09.jdbc.dao.AccountDaoJdbcMapper;
import pro.kuli4.otus.java.hw09.jdbc.dao.UserDaoJdbcMapper;
import pro.kuli4.otus.java.hw09.jdbc.mapper.*;
import pro.kuli4.otus.java.hw09.jdbc.sessionmanager.SessionManagerJdbc;

import javax.sql.DataSource;
import java.math.BigDecimal;

/*
* ./gradlew hw09-orm:clean hw09-orm:build
* java -jar hw09-orm/build/libs/hw09-orm-0.0.1.jar
 */

@Slf4j
public class HomeWork {

    public static void main(String[] args) {
// Общая часть
        var dataSource = new DataSourceH2();
        flywayMigrations(dataSource);
        var sessionManager = new SessionManagerJdbc(dataSource);

// Работа с пользователем
        DbExecutorImpl<User> dbExecutor = new DbExecutorImpl<>();
        EntityClassMetaData<User> userEntityClassMetaData = new EntityClassMetaDataImpl<>(User.class);
        EntitySQLMetaData userEntitySqlMetaData = new EntitySQLMetaDataImpl(userEntityClassMetaData);
        JdbcMapper<User> jdbcMapperUser = new JdbcMapperImpl<>(dbExecutor, sessionManager, userEntityClassMetaData, userEntitySqlMetaData);
        UserDao userDao = new UserDaoJdbcMapper(sessionManager, jdbcMapperUser);

// Код дальше должен остаться, т.е. userDao должен использоваться
        var dbServiceUser = new DbServiceUserImpl(userDao);
        var user = new User("Alex", 30);
        var id = dbServiceUser.saveUser(user);
        var dbUser = dbServiceUser.getUser(id).orElse(null);
        log.info("Program user: {}", user);
        log.info("User from db: {}", dbUser);
        log.info("Equals: {}", user.equals(dbUser));
        dbUser.setAge(44);
        id = dbServiceUser.saveUser(dbUser);
        log.info("Changed user: {}", dbServiceUser.getUser(id).orElse(null));

// Работа со счетом
        DbExecutorImpl<Account> dbExecutorAccount = new DbExecutorImpl<>();
        EntityClassMetaData<Account> accountEntityClassMetaData = new EntityClassMetaDataImpl<>(Account.class);
        EntitySQLMetaData accountEntitySqlMetaData = new EntitySQLMetaDataImpl(accountEntityClassMetaData);
        JdbcMapper<Account> jdbcMapperAccount = new JdbcMapperImpl<>(dbExecutorAccount, sessionManager, accountEntityClassMetaData, accountEntitySqlMetaData);
        AccountDao accountDao = new AccountDaoJdbcMapper(sessionManager, jdbcMapperAccount);

        var dbServiceAccount = new DbServiceAccountImpl(accountDao);
        var account = new Account("credit", new BigDecimal("30.34"));
        id = dbServiceAccount.saveAccount(account);
        var dbAccount = dbServiceAccount.getAccount(id).orElse(null);
        log.info("Program account: {}", account);
        log.info("Account from db: {}", dbAccount);
        log.info("Equals: {}", account.equals(dbAccount));
        dbAccount.setRest(new BigDecimal("44.44"));
        id = dbServiceAccount.saveAccount(dbAccount);
        log.info("Changed account: {}", dbServiceAccount.getAccount(id).orElse(null));
    }

    private static void flywayMigrations(DataSource dataSource) {
        log.info("db migration started...");
        var flyway = Flyway.configure()
                .dataSource(dataSource)
                .locations("classpath:/db/migration")
                .load();
        flyway.migrate();
        log.info("db migration finished.");
        log.info("***");
    }
}

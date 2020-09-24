package pro.kuli4.otus.java.hw09.core.sessionmanager;

import java.sql.Connection;

public interface DatabaseSession {
    Connection getConnection();
}

package pro.kuli4.otus.java.hw12.dao;

public class UserDaoException extends RuntimeException {
    public UserDaoException(String message, Exception ex) {
        super(message, ex);
    }
}

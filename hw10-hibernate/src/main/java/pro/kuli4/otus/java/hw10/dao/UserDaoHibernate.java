package pro.kuli4.otus.java.hw10.dao;


import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import pro.kuli4.otus.java.hw10.entities.User;

import java.util.Optional;

@Slf4j
public class UserDaoHibernate implements UserDao {

    private final SessionFactory sessionFactory;

    public UserDaoHibernate(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


    @Override
    public Optional<User> findById(long id) {
        try(Session session = sessionFactory.openSession()) {
            return Optional.ofNullable(session.find(User.class, id));
        } catch (Exception e) {
            throw new UserDaoException(e);
        }
    }

    @Override
    public long insertUser(User user) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void updateUser(User user) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void insertOrUpdate(User user) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.getTransaction();
            tx.begin();
            if (user.getId() != null) {
                session.merge(user);
            } else {
                session.save(user);
            }
            tx.commit();
        } catch (Exception e) {
            throw new UserDaoException(e);
        }
    }
}

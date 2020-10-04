package pro.kuli4.otus.java.hw12.dto;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import java.util.List;

@Slf4j
public class UserJsonDtoHibernate implements UserJsonDto {

    private final SessionFactory sessionFactory;
    private final Gson gson;

    public UserJsonDtoHibernate(SessionFactory sessionFactory, Gson gson) {
        this.sessionFactory = sessionFactory;
        this.gson = gson;
    }

    @Override
    public String getAllUsers() {
        try (var session = sessionFactory.openSession()) {
            var query = session.createQuery("from User"); //#TODO Resolve N+1 problem
            List<?> users = query.list();
            return gson.toJson(users);
        }
    }
}

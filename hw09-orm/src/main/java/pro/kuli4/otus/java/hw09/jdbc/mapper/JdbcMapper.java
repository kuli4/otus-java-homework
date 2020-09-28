package pro.kuli4.otus.java.hw09.jdbc.mapper;

import java.util.Optional;

/**
 * Сохратяет объект в базу, читает объект из базы
 *
 * @param <T>
 */
public interface JdbcMapper<T> {
    Object insert(T objectData);

    Object update(T objectData);

    Object insertOrUpdate(T objectData);

    Optional<T> findById(Object id, Class<T> clazz);
}

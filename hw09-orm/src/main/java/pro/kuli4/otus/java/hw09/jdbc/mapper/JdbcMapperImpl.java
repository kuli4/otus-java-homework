package pro.kuli4.otus.java.hw09.jdbc.mapper;

import lombok.extern.slf4j.Slf4j;
import pro.kuli4.otus.java.hw09.core.sessionmanager.SessionManager;
import pro.kuli4.otus.java.hw09.jdbc.DbExecutor;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
public class JdbcMapperImpl<T> implements JdbcMapper<T> {

    private final DbExecutor<T> dbExecutor;
    private final EntityClassMetaData<T> entityClassMetaData;
    private final EntitySQLMetaData entitySQLMetaData;
    private final SessionManager sessionManager;

    public JdbcMapperImpl(DbExecutor<T> dbExecutor, SessionManager sessionManager, EntityClassMetaData<T> entityClassMetaData, EntitySQLMetaData entitySQLMetaData) {
        this.dbExecutor = dbExecutor;
        this.sessionManager = sessionManager;
        this.entityClassMetaData = entityClassMetaData;
        this.entitySQLMetaData = entitySQLMetaData;
    }

    @Override
    public Object insert(T objectData) {
        try {
            long id = dbExecutor.executeInsert(sessionManager.getCurrentSession().getConnection(),
                    entitySQLMetaData.getInsertSql(),
                    entityClassMetaData.getFieldsWithoutId().stream().map(field -> {
                        field.setAccessible(true);
                        try {
                            return field.get(objectData);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }).collect(Collectors.toList())
            );
            Field idField = entityClassMetaData.getIdField();
            idField.setAccessible(true);
            idField.set(objectData, id);
            return id;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Object update(T objectData) {
        try {
            List<Object> args = entityClassMetaData.getFieldsWithoutId().stream().map(field -> {
                field.setAccessible(true);
                try {
                    return field.get(objectData);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }).collect(Collectors.toList());
            args.add(entityClassMetaData.getIdField().get(objectData));
            return dbExecutor.executeInsert(sessionManager.getCurrentSession().getConnection(), entitySQLMetaData.getUpdateSql(), args);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Object insertOrUpdate(T objectData) {
        try {
            Field idField = entityClassMetaData.getIdField();
            idField.setAccessible(true);
            long id = (long) idField.get(objectData);
            if (id != 0) {
                log.debug("Update!");
                return update(objectData);
            } else {
                log.debug("Insert!");
                return insert(objectData);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<T> findById(Object id, Class<T> clazz) {
        try {

            return dbExecutor.executeSelect(sessionManager.getCurrentSession().getConnection(),
                    entitySQLMetaData.getSelectByIdSql(),
                    id,
                    rs -> {
                        try {
                            if (rs.next()) {
                                T entity = entityClassMetaData.getConstructor().newInstance();
                                for (Field field : entityClassMetaData.getAllFields()) {
                                    field.setAccessible(true);
                                    field.set(entity, rs.getObject(field.getName()));
                                }
                                return entity;
                            }
                        } catch (Exception e) {
                            log.error(e.getMessage(), e);
                        }
                        return null;
                    });
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

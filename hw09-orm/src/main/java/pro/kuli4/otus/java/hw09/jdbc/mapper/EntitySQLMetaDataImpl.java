package pro.kuli4.otus.java.hw09.jdbc.mapper;

import java.lang.reflect.Field;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EntitySQLMetaDataImpl implements EntitySQLMetaData {

    private final EntityClassMetaData<?> entityClassMetaData;

    public EntitySQLMetaDataImpl(EntityClassMetaData<?> entityClassMetaData) {
        this.entityClassMetaData = entityClassMetaData;
    }

    @Override
    public String getSelectAllSql() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getSelectByIdSql() {
        StringBuilder select = new StringBuilder("select ");
        for (Field field : entityClassMetaData.getAllFields()) {
            select.append(field.getName()).append(",");
        }
        select.delete(select.length() - 1, select.length())
                .append(" from ")
                .append(entityClassMetaData.getName())
                .append(" where ")
                .append(entityClassMetaData.getIdField().getName())
                .append(" = ?");
        log.debug("Generated select: " + select.toString().toUpperCase());
        return select.toString().toUpperCase();
    }

    @Override
    public String getInsertSql() {
        StringBuilder insert = new StringBuilder("insert into ");
        insert.append(entityClassMetaData.getName())
                .append(" (");
        StringBuilder values = new StringBuilder("values (");
        for (Field field : entityClassMetaData.getFieldsWithoutId()) {
            insert.append(field.getName())
                    .append(",");
            values.append("?,");
        }
        values.delete(values.length() - 1, values.length())
                .append(") ");
        insert.delete(insert.length() - 1, insert.length())
                .append(") ")
                .append(values);
        log.debug("Generated insert: " + insert.toString().toUpperCase());
        return insert.toString().toUpperCase();
    }

    @Override
    public String getUpdateSql() {
        StringBuilder update = new StringBuilder("update ");
        update.append(entityClassMetaData.getName())
                .append(" set ");
        for (Field field : entityClassMetaData.getFieldsWithoutId()) {
            update.append(field.getName())
                    .append(" = ?,");
        }
        update.delete(update.length() - 1, update.length())
                .append(" where ")
                .append(entityClassMetaData.getIdField().getName())
                .append(" = ?");
        log.debug("Generated update: " + update.toString().toUpperCase());
        return update.toString().toUpperCase();
    }
}

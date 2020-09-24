package pro.kuli4.otus.java.hw09.jdbc.mapper;

import pro.kuli4.otus.java.hw09.core.model.annotations.Id;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class EntityClassMetaDataImpl<T> implements EntityClassMetaData<T> {

    private final Class<T> clazz;
    private final Field idField;
    private final List<Field> fields;

    public EntityClassMetaDataImpl(Class<T> clazz) {
        this.clazz = clazz;
        idField = Arrays.stream(clazz.getDeclaredFields()).filter(field -> field.isAnnotationPresent(Id.class)).findFirst().get();
        fields = Arrays.stream(clazz.getDeclaredFields()).filter(field -> !field.equals(idField)).collect(Collectors.toList());
    }

    @Override
    public String getName() {
        return clazz.getSimpleName();
    }

    @Override
    public Constructor<T> getConstructor() {
        try {

            return clazz.getConstructor();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public Field getIdField() {
        return idField;
    }

    @Override
    public List<Field> getAllFields() {
        List<Field> allFields = new ArrayList<>(fields);
        allFields.add(idField);
        return allFields;
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        return List.copyOf(fields);
    }
}

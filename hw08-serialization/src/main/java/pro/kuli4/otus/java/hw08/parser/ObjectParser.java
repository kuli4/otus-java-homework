package pro.kuli4.otus.java.hw08.parser;

import pro.kuli4.otus.java.hw08.visitor.ArrayVisitor;
import pro.kuli4.otus.java.hw08.visitor.CollectionVisitor;
import pro.kuli4.otus.java.hw08.visitor.ObjectVisitor;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;

public class ObjectParser implements Parser {

    private final ObjectVisitor ov;

    public ObjectParser(ObjectVisitor ov) {
        this.ov = ov;
    }

    @Override
    public void parse(Object o) {
        this.parse(o, ov);
    }

    protected void parse(Object o, ObjectVisitor ov) {
        ov.visit();
        try {
            Class<?> clazz = o.getClass();
            for (Field field : clazz.getDeclaredFields()) {
                // Check field for static
                if ((field.getModifiers() & Modifier.STATIC) != 0) continue;

                // Set field accessible
                field.setAccessible(true);

                //Parse primitive
                if (field.getType().isPrimitive()) {
                    if (field.getType().equals(int.class) || field.getType().equals(byte.class) || field.getType().equals(short.class)) {
                        ov.visitInt(field.getName(), field.getInt(o));
                    } else if (field.getType().equals(long.class)) {
                        ov.visitLong(field.getName(), field.getLong(o));
                    } else if (field.getType().equals(char.class)) {
                        ov.visitChar(field.getName(), field.getChar(o));
                    } else if (field.getType().equals(boolean.class)) {
                        ov.visitBoolean(field.getName(), field.getBoolean(o));
                    } else if (field.getType().equals(float.class) || field.getType().equals(double.class)) {
                        ov.visitDouble(field.getName(), field.getDouble(o));
                    }
                    continue;
                }

                // Check field for null
                if (field.get(o) == null) {
                    ov.visitNull(field.getName());
                    continue;
                }

                // Parse Collection
                if (Collection.class.isAssignableFrom(field.getType())) {
                    CollectionVisitor cv = ov.visitCollection(field.getName(), o);
                    Parser ap = new CollectionParser(cv);
                    ap.parse(field.get(o));
                    continue;
                }

                // Parse array
                if (field.getType().isArray()) {
                    ArrayVisitor av = ov.visitArray(field.getName(), o);
                    Parser ap = new ArrayParser(av);
                    ap.parse(field.get(o));
                    continue;
                }

                // Parse String
                if (field.getType().equals(String.class)) {
                    ov.visitString(field.getName(), (String) field.get(o));
                    continue;
                }


                // Parse boxed int, short, byte
                if (field.getType().equals(Integer.class) || field.getType().equals(Short.class) || field.getType().equals(Byte.class)) {
                    ov.visitInt(field.getName(), (int) field.get(o));
                    continue;
                }

                // Parse boxed long
                if (field.getType().equals(Long.class)) {
                    ov.visitLong(field.getName(), (long) field.get(o));
                    continue;
                }

                // Parse boxed char
                if (field.getType().equals(Character.class)) {
                    ov.visitChar(field.getName(), (char) field.get(o));
                    continue;
                }

                // Parse boxed boolean
                if (field.getType().equals(Boolean.class)) {
                    ov.visitBoolean(field.getName(), (boolean) field.get(o));
                    continue;
                }

                //Parse boxed float, double
                if (field.getType().equals(Float.class) || field.getType().equals(Double.class)) {
                    ov.visitDouble(field.getName(), (double) field.get(o));
                    continue;
                }


                // Parse other Object
                this.parse(field.get(o), ov.visitObject(field.getName(), field.get(o)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        ov.visitEnd();
    }
}

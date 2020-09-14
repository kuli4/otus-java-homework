package pro.kuli4.otus.java.hw08.parser;

import pro.kuli4.otus.java.hw08.visitor.ArrayVisitor;
import pro.kuli4.otus.java.hw08.visitor.CollectionVisitor;
import pro.kuli4.otus.java.hw08.visitor.ObjectVisitor;

import java.lang.reflect.Array;
import java.util.Collection;

public class ArrayParser implements Parser {

    private final ArrayVisitor av;

    public ArrayParser(ArrayVisitor av) {
        this.av = av;
    }

    @Override
    public void parse(Object o) {
        this.parse(o, av);
    }

    protected void parse(Object o, ArrayVisitor av) {
        av.visit();
        Class<?> elementType = o.getClass().getComponentType();

        for (int i = 0; i < Array.getLength(o); i++) {

            // Check field for null
            if (Array.get(o, i) == null) {
                av.visitNull();
                continue;
            }

            //Parse primitive
            if (elementType.isPrimitive()) {
                if (elementType.equals(int.class) || elementType.equals(byte.class) || elementType.equals(short.class)) {
                    av.visitInt(Array.getInt(o, i));
                } else if (elementType.equals(long.class)) {
                    av.visitLong(Array.getLong(o, i));
                } else if (elementType.equals(char.class)) {
                    av.visitChar(Array.getChar(o, i));
                } else if (elementType.equals(boolean.class)) {
                    av.visitBoolean(Array.getBoolean(o, i));
                } else if (elementType.equals(float.class) || elementType.equals(double.class)) {
                    av.visitDouble(Array.getDouble(o, i));
                }
                continue;
            }

            // Parse array
            if (elementType.isArray()) {
                this.parse(Array.get(o, i), av.visitArray(Array.get(o, i)));
                continue;
            }

            // Parse Collection
            if (Collection.class.isAssignableFrom(Array.get(o, i).getClass())) {
                CollectionVisitor cv = av.visitCollection(o);
                Parser ap = new CollectionParser(cv);
                ap.parse(Array.get(o, i));
                continue;
            }

            // Parse String
            if (elementType.equals(String.class)) {
                av.visitString((String) Array.get(o, i));
                continue;
            }

            // Parse boxed int, short, byte
            if (elementType.equals(Integer.class) || elementType.equals(Short.class) || elementType.equals(Byte.class)) {
                av.visitInt((int) Array.get(o,i));
                continue;
            }

            // Parse boxed long
            if (elementType.equals(Long.class)) {
                av.visitLong((long) Array.get(o,i));
                continue;
            }

            // Parse boxed char
            if (elementType.equals(Character.class)) {
                av.visitChar((char) Array.get(o,i));
                continue;
            }

            // Parse boxed boolean
            if (elementType.equals(Boolean.class)) {
                av.visitBoolean((boolean) Array.get(o,i));
                continue;
            }

            //Parse boxed float, double
            if (elementType.equals(Float.class) || elementType.equals(Double.class)) {
                av.visitDouble((double) Array.get(o,i));
                continue;
            }

            // Parse other Object
            ObjectVisitor ov = av.visitObject(o);
            Parser ap = new ObjectParser(ov);
            ap.parse(Array.get(o, i));
        }
        av.visitEnd();
    }
}

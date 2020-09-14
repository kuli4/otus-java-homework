package pro.kuli4.otus.java.hw08.parser;

import pro.kuli4.otus.java.hw08.visitor.ArrayVisitor;
import pro.kuli4.otus.java.hw08.visitor.CollectionVisitor;
import pro.kuli4.otus.java.hw08.visitor.ObjectVisitor;

import java.lang.reflect.Array;
import java.util.Collection;

public class CollectionParser implements Parser {

    private final CollectionVisitor cv;

    public CollectionParser(CollectionVisitor cv) {
        this.cv = cv;
    }

    @Override
    public void parse(Object o) {
        this.parse(o, cv);
    }

    protected void parse(Object o, CollectionVisitor cv) {
        cv.visit();
        Collection<?> collection = (Collection<?>) o;
        for (Object ob : collection) {

            // Check field for null
            if (ob == null) {
                cv.visitNull();
                continue;
            }

            // Parse String
            if (ob instanceof String) {
                cv.visitString((String) ob);
                continue;
            }

            // Parse array
            if (ob.getClass().isArray()) {
                ArrayVisitor av = cv.visitArray(ob);
                Parser ap = new ArrayParser(av);
                ap.parse(ob);
                continue;
            }

            // Parse Collection
            if (Collection.class.isAssignableFrom(ob.getClass())) {
                this.parse(ob, cv);
                continue;
            }

            // Parse boxed int, short, byte
            if (ob.getClass().equals(Integer.class) || ob.getClass().equals(Short.class) || ob.getClass().equals(Byte.class)) {
                cv.visitInt((int) ob);
                continue;
            }

            // Parse boxed long
            if (ob.getClass().equals(Long.class)) {
                cv.visitLong((long) ob);
                continue;
            }

            // Parse boxed char
            if (ob.getClass().equals(Character.class)) {
                cv.visitChar((char) ob);
                continue;
            }

            // Parse boxed boolean
            if (ob.getClass().equals(Boolean.class)) {
                cv.visitBoolean((boolean) ob);
                continue;
            }

            //Parse boxed float, double
            if (ob.getClass().equals(Float.class) || ob.getClass().equals(Double.class)) {
                cv.visitDouble((double) ob);
                continue;
            }

            // Parse other Object
            ObjectVisitor ov = cv.visitObject(ob);
            Parser ap = new ObjectParser(ov);
            ap.parse(ob);
        }
        cv.visitEnd();
    }

}

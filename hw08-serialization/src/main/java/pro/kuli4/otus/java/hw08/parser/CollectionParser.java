package pro.kuli4.otus.java.hw08.parser;

import pro.kuli4.otus.java.hw08.visitor.ArrayVisitor;
import pro.kuli4.otus.java.hw08.visitor.CollectionVisitor;
import pro.kuli4.otus.java.hw08.visitor.ObjectVisitor;

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

            // Parse other Object
            ObjectVisitor ov = cv.visitObject(ob);
            Parser ap = new ObjectParser(ov);
            ap.parse(ob);
        }
        cv.visitEnd();
    }

}

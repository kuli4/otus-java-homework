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
                switch (elementType.getName()) {
                    case "int":
                    case "byte":
                    case "short":
                        av.visitInt(Array.getInt(o, i));
                        break;
                    case "long":
                        av.visitLong(Array.getLong(o, i));
                        break;
                    case "char":
                        av.visitChar(Array.getChar(o, i));
                        break;
                    case "boolean":
                        av.visitBoolean(Array.getBoolean(o, i));
                        break;
                    case "float":
                    case "double":
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

            // Parse other Object
            ObjectVisitor ov = av.visitObject(o);
            Parser ap = new ObjectParser(ov);
            ap.parse(Array.get(o, i));
        }
        av.visitEnd();
    }
}

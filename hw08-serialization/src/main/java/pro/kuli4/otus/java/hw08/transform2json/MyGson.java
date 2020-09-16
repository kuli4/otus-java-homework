package pro.kuli4.otus.java.hw08.transform2json;

import pro.kuli4.otus.java.hw08.parser.ArrayParser;
import pro.kuli4.otus.java.hw08.parser.CollectionParser;
import pro.kuli4.otus.java.hw08.parser.ObjectParser;
import pro.kuli4.otus.java.hw08.parser.Parser;

import java.io.ByteArrayOutputStream;
import java.util.Collection;

public class MyGson {
    public String toJson(Object o) {
        if (o == null) {
            return "null";
        }
        if (o.getClass().isArray()) {
            Array2Json av = new Array2Json();
            Parser p = new ArrayParser(av);
            p.parse(o);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            av.buildJson(baos);
            return new String(baos.toByteArray());
        } else if (Collection.class.isAssignableFrom(o.getClass())) {
            Collection2Json cv = new Collection2Json();
            Parser p = new CollectionParser(cv);
            p.parse(o);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            cv.buildJson(baos);
            return new String(baos.toByteArray());
        } else {

            if (o.getClass().equals(Integer.class) || o.getClass().equals(Short.class) || o.getClass().equals(Byte.class)) {
                return toJson((int) o);
            }

            if (o.getClass().equals(Long.class)) {
                return toJson((long) o);
            }

            if (o.getClass().equals(Character.class)) {
                return toJson((char) o);
            }

            if (o.getClass().equals(Boolean.class)) {
                return toJson((boolean) o);
            }

            if (o.getClass().equals(Float.class) || o.getClass().equals(Double.class)) {
                return toJson((double) o);
            }

            Object2Json ov = new Object2Json();
            Parser p = new ObjectParser(ov);
            p.parse(o);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ov.buildJson(baos);
            return new String(baos.toByteArray());
        }
    }

    public String toJson(String str) {
        return "\"" + str + "\"";
    }

    public String toJson(int i) {
        return String.valueOf(i);
    }

    public String toJson(long l) {
        return String.valueOf(l);
    }

    public String toJson(double d) {
        return String.valueOf(d);
    }

    public String toJson(char c) {
        return "\"" + c + "\"";
    }

    public String toJson(boolean b) {
        return String.valueOf(b);
    }
}

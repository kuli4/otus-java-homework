package pro.kuli4.otus.java.hw08;

import pro.kuli4.otus.java.hw08.parser.ArrayParser;
import pro.kuli4.otus.java.hw08.parser.ObjectParser;
import pro.kuli4.otus.java.hw08.parser.Parser;
import pro.kuli4.otus.java.hw08.transform2json.Array2Json;
import pro.kuli4.otus.java.hw08.transform2json.Object2Json;

import java.io.ByteArrayOutputStream;

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
        } else {
            Object2Json ov = new Object2Json();
            Parser p = new ObjectParser(ov);
            p.parse(o);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ov.buildJson(baos);
            return new String(baos.toByteArray());
        }
    }
}

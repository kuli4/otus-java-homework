package pro.kuli4.otus.java.hw08.transform2json;

import pro.kuli4.otus.java.hw08.visitor.ArrayVisitor;
import pro.kuli4.otus.java.hw08.visitor.CollectionVisitor;
import pro.kuli4.otus.java.hw08.visitor.ObjectVisitor;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonStructure;
import javax.json.JsonWriter;
import java.io.OutputStream;

public class Collection2Json extends Any2Json implements CollectionVisitor {

    private final JsonArrayBuilder jab;

    public Collection2Json() {
        jab = Json.createArrayBuilder();
    }

    @Override
    void addChild(String name, JsonStructure js) {
        jab.add(js);
    }

    @Override
    public void visit() {
    }

    @Override
    public void visitInt(int i) {
        jab.add(i);
    }

    @Override
    public void visitLong(long l) {
        jab.add(l);
    }

    @Override
    public void visitDouble(double d) {
        jab.add(d);
    }

    @Override
    public void visitBoolean(boolean b) {
        jab.add(b);
    }

    @Override
    public void visitChar(char c) {
        jab.add(String.valueOf(c));
    }

    @Override
    public void visitString(String str) {
        jab.add(str);
    }

    @Override
    public ArrayVisitor visitArray(Object o) {
        Array2Json array2Json = new Array2Json();
        array2Json.setWrapper(this);
        return array2Json;
    }

    @Override
    public ObjectVisitor visitObject(Object o) {
        Object2Json object2Json = new Object2Json();
        object2Json.setWrapper(this);
        return object2Json;
    }

    @Override
    public CollectionVisitor visitCollection(Object o) {
        Collection2Json collection2Json = new Collection2Json();
        collection2Json.setWrapper(this);
        return collection2Json;
    }

    @Override
    public void visitNull() {
        jab.addNull();
    }

    @Override
    public void visitEnd() {
        if (getWrapper() != null) {
            getWrapper().addChild(name, jab.build());
        }
    }

    public void buildJson(OutputStream out) {
        JsonWriter jw = Json.createWriter(out);
        jw.write(jab.build());
        jw.close();
    }
}

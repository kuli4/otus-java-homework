package pro.kuli4.otus.java.hw08.transform2json;

import pro.kuli4.otus.java.hw08.visitor.ArrayVisitor;
import pro.kuli4.otus.java.hw08.visitor.CollectionVisitor;
import pro.kuli4.otus.java.hw08.visitor.ObjectVisitor;

import javax.json.*;
import java.io.OutputStream;

public class Object2Json extends Any2Json implements ObjectVisitor {

    private final JsonObjectBuilder job;

    public Object2Json() {
        job = Json.createObjectBuilder();
    }

    @Override
    public void visit() {
    }

    @Override
    public ArrayVisitor visitArray(String name, Object o) {
        Array2Json array2Json = new Array2Json();
        array2Json.setWrapper(this);
        array2Json.setName(name);
        return array2Json;
    }

    @Override
    public ObjectVisitor visitObject(String name, Object o) {
        Object2Json object2Json = new Object2Json();
        object2Json.setWrapper(this);
        object2Json.setName(name);
        return object2Json;
    }

    @Override
    public CollectionVisitor visitCollection(String name, Object o) {
        Collection2Json collection2Json = new Collection2Json();
        collection2Json.setWrapper(this);
        collection2Json.setName(name);
        return collection2Json;
    }

    @Override
    public void visitInt(String name, int i) {
        job.add(name, i);
    }

    @Override
    public void visitLong(String name, long l) {
        job.add(name, l);
    }

    @Override
    public void visitDouble(String name, double d) {
        job.add(name, d);
    }

    @Override
    public void visitBoolean(String name, boolean b) {
        job.add(name, b);
    }

    @Override
    public void visitChar(String name, char c) {
        job.add(name, String.valueOf(c));
    }

    @Override
    public void visitString(String name, String str) {
        job.add(name, str);
    }

    @Override
    public void visitNull(String name) {
        job.addNull(name);
    }

    @Override
    public void visitEnd() {
        if (getWrapper() != null) {
            getWrapper().addChild(name, job.build());
        }
    }

    public void buildJson(OutputStream out) {
        JsonWriter jw = Json.createWriter(out);
        jw.write(job.build());
        jw.close();
    }

    @Override
    void addChild(String name, JsonStructure js) {
        job.add(name, js);
    }
}

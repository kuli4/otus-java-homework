package pro.kuli4.otus.java.hw08.visitor;

public interface ObjectVisitor {
    void visit();

    void visitInt(String name, int i);

    void visitLong(String name, long l);

    void visitDouble(String name, double d);

    void visitBoolean(String name, boolean b);

    void visitChar(String name, char c);

    void visitString(String name, String str);

    ArrayVisitor visitArray(String name, Object o);

    ObjectVisitor visitObject(String name, Object o);

    CollectionVisitor visitCollection(String name, Object o);

    void visitNull(String name);

    void visitEnd();
}

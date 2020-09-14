package pro.kuli4.otus.java.hw08.visitor;

public interface CollectionVisitor {
    void visit();

    void visitInt(int i);

    void visitLong(long l);

    void visitDouble(double d);

    void visitBoolean(boolean b);

    void visitChar(char c);

    void visitString(String str);

    ArrayVisitor visitArray(Object o);

    ObjectVisitor visitObject(Object o);

    CollectionVisitor visitCollection(Object o);

    void visitNull();

    void visitEnd();
}

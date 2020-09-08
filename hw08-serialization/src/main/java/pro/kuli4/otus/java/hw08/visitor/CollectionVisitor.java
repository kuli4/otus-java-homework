package pro.kuli4.otus.java.hw08.visitor;

public interface CollectionVisitor {
    void visit();


    void visitString(String str);

    ArrayVisitor visitArray(Object o);

    ObjectVisitor visitObject(Object o);

    CollectionVisitor visitCollection(Object o);

    void visitNull();

    void visitEnd();
}

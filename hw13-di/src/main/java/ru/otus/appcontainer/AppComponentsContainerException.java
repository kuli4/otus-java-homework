package ru.otus.appcontainer;

public class AppComponentsContainerException extends RuntimeException {
    public AppComponentsContainerException(Throwable e) {
        super(e);
    }

    public AppComponentsContainerException(String str) {
        super(str);
    }
}

package pro.kuli4.otus.java.hw03.framework;

import pro.kuli4.otus.java.hw03.framework.interfaces.TestRunner;

public class Launcher {

    public static <T> void test(Class<T> clazz) {
        TestRunner testRunner = new SimpleTestRunnerImpl();
        testRunner.run(clazz);
    }

}

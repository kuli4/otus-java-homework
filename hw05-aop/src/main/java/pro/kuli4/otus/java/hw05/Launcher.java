package pro.kuli4.otus.java.hw05;

/*
 * ./gradlew hw05-aop:clean hw05-aop:build
 * java -javaagent:hw05-aop/build/libs/hw05-aop-0.0.1.jar -jar hw05-aop/build/libs/hw05-aop-0.0.1.jar
 */

public class Launcher {
    public static void main(String[] args) {
        TestLogger testLogger = new TestLogger(10);
        System.out.println(testLogger.print());
        testLogger.printString("Hello!");
        short s = 3;
        testLogger.print(10L, s);
    }
}

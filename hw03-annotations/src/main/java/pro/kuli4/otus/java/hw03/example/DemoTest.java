package pro.kuli4.otus.java.hw03.example;

import pro.kuli4.otus.java.hw03.framework.Launcher;
import pro.kuli4.otus.java.hw03.framework.annotations.*;
import pro.kuli4.otus.java.hw03.framework.exceptions.*;

/*
 * ./gradlew :hw03-annotations:clean build
 * java -jar ./hw03-annotations/build/libs/hw03-annotations-0.0.1.jar
 */

public class DemoTest {

    public static void main(String[] args) {
        Launcher.test(DemoTest.class);
    }

    @Before
    public void setUp() {
        System.out.println("setUp");
    }

    @Before
    public void setUp1() {
        System.out.println("setUp1");
    }

    @Test
    public void testInt() {
        System.out.println("test");
    }

    @Test
    public void testString() {
        System.out.println("test1");
        throw new CustomAssertException();
    }

    @After
    public void tearDown() {
        System.out.println("tearDown");
    }
}

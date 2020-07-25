package pro.kuli4.otus.java.hw03.framework;

import java.lang.reflect.Method;
import java.util.Arrays;

import pro.kuli4.otus.java.hw03.framework.interfaces.TestRunner;
import pro.kuli4.otus.java.hw03.framework.annotations.*;

import static pro.kuli4.otus.java.hw03.framework.ReflectionHelper.*;

public class SimpleTestRunnerImpl implements TestRunner {

    private int countOfTest, countOfFailedTest, countOfSuccessTest;
    private Method[] beforeMethods, testMethods, afterMethods;

    @Override
    public <T> void run(Class<T> clazz) {
        if (clazz.isArray()) {
            System.out.println("The Test Framework does not work with arrays!");
            return;
        }
        if (clazz.isPrimitive()) {
            System.out.println("The Test Framework does not work with primitives!");
            return;
        }

        beforeMethods = Arrays.stream(clazz.getDeclaredMethods()).filter(method -> method.isAnnotationPresent(Before.class)).toArray(Method[]::new);
        testMethods = Arrays.stream(clazz.getDeclaredMethods()).filter(method -> method.isAnnotationPresent(Test.class)).toArray(Method[]::new);
        afterMethods = Arrays.stream(clazz.getDeclaredMethods()).filter(method -> method.isAnnotationPresent(After.class)).toArray(Method[]::new);

        countOfTest = testMethods.length;

        if (countOfTest == 0) {
            System.out.println("Don't found test cases for " + clazz.getCanonicalName());
            return;
        }

        T object;

        for (Method testMethod : testMethods) {

            object = instantiate(clazz);

            for (Method beforeMethod : beforeMethods) {
                callMethod(object, beforeMethod.getName());
            }

            try {
                callMethod(object, testMethod.getName());
                countOfSuccessTest++;
            } catch (Exception e) {
                countOfFailedTest++;
            }

            for (Method afterMethod : afterMethods) {
                callMethod(object, afterMethod.getName());
            }

        }

        System.out.println("=============================");
        System.out.println("Tests: " + countOfTest + ", failed: " + countOfFailedTest + ", success: " + countOfSuccessTest);
        System.out.println("=============================");
    }

}

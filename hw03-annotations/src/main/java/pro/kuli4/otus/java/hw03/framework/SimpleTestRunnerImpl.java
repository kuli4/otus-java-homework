package pro.kuli4.otus.java.hw03.framework;

import java.lang.reflect.Method;
import java.util.Arrays;

import pro.kuli4.otus.java.hw03.framework.interfaces.TestRunner;
import pro.kuli4.otus.java.hw03.framework.annotations.*;

import static pro.kuli4.otus.java.hw03.framework.ReflectionHelper.*;

public class SimpleTestRunnerImpl implements TestRunner {

    @Override
    public <T> void run(Class<T> clazz) {

        Method[] beforeMethods = Arrays.stream(clazz.getDeclaredMethods()).filter(method -> method.isAnnotationPresent(Before.class)).toArray(Method[]::new);
        Method[] testMethods = Arrays.stream(clazz.getDeclaredMethods()).filter(method -> method.isAnnotationPresent(Test.class)).toArray(Method[]::new);
        Method[] afterMethods = Arrays.stream(clazz.getDeclaredMethods()).filter(method -> method.isAnnotationPresent(After.class)).toArray(Method[]::new);

        int countOfTest = testMethods.length;
        int countOfFailedTest = 0;
        int countOfSuccessTest = 0;

        if (countOfTest == 0) {
            System.out.println("Don't found test cases for " + clazz.getCanonicalName());
            return;
        }

        for (Method testMethod : testMethods) {

            T object = instantiate(clazz);

            try {
                for (Method beforeMethod : beforeMethods) {
                    callMethod(object, beforeMethod);
                }
                try {
                    callMethod(object, testMethod);
                    countOfSuccessTest++;
                } catch (RuntimeException e) {
                    countOfFailedTest++;
                }
            } finally {
                for (Method afterMethod : afterMethods) {
                    try {
                        callMethod(object, afterMethod);
                    } catch (RuntimeException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        System.out.println("=============================");
        System.out.println("Tests: " + countOfTest + ", failed: " + countOfFailedTest + ", success: " + countOfSuccessTest);
        System.out.println("=============================");
    }

}

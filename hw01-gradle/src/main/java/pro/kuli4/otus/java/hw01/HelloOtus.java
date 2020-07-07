package pro.kuli4.otus.java.hw01;

import com.google.common.collect.Range;


/**
 *
 * To start the application:
 * ./gradlew build
 * java -jar ./hw01-gradle/build/libs/hw01-0.1.jar
 *
 */
public class HelloOtus {
    public static void main(String[] args) {
        Range<Integer> range = Range.closedOpen(0, 9);

        System.out.println("Range : " + range);
        System.out.println("Upper bound: " + range.upperEndpoint());
        System.out.println("9 is present: " + range.contains(9));
    }
}
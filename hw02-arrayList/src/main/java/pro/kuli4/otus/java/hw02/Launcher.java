package pro.kuli4.otus.java.hw02;

import java.util.List;
import java.util.Collections;

/*
 * ./gradlew clean build
 * java -jar ./hw02-arrayList/build/libs/hw02-arrayList-0.0.1.jar
 *
 *
 */

public class Launcher {
    public static void main(String[] args) {
        if (args.length > 0) {
            String command = args[0];
            if (command.equals("int")) {
                testInteger();
            } else if (command.equals("str")) {
                testStrings();
            }
        } else {
            testStrings();
            testInteger();
        }
    }

    public static void testStrings() {
        List<String> str1 = new DIYArrayList<>("Hello", "Hi", "Good");
        List<String> str2 = new DIYArrayList<>();
        List<String> str3 = new DIYArrayList<>("he");
        System.out.println("str2 - isEmpty(), must be \"true\" :" + str2.isEmpty());
        System.out.println("str1 - isEmpty(), must by \"false\" :" + str1.isEmpty());
        System.out.println("str1 - size(), must be 3: " + str1.size());
        str2.add("Join");
        System.out.println("Added element to str2, get at index, must be \"Join\"" + str2.get(0));
        System.out.println("Print all elements of str1 by enhanced loop:");
        for(String str: str1) {
            System.out.println("\t" + str);
        }
        Collections.addAll(str2, "Bybyby", "Lous", "Goooo", "Helen", "Joan", "Petr", "Sergei", "Ivan", "Leo", "Geo", "She", "Gram", "Liam", "William", "Snack", "Can", "Box", "Barker", "Jule", "Lost");
        System.out.println("str2 after adding: " + str2);
        System.out.println("str2 size: " + str2.size());
        Collections.copy(str2, str3);
        System.out.println("str2 after copping: " + str2);
        Collections.sort(str2);
        System.out.println("str2 after sorting: " + str2);
    }

    public static void testInteger() {
        List<Integer> integers1 = new DIYArrayList<>();
        List<Integer> integers2 = new DIYArrayList<>();
        int count = 0;
        while(count++ < 100) {
            integers1.add((int) (Math.random()*100));
        }
        while (count-- > 0) {
            integers2.add(count);
        }
        System.out.println("integers1: " + integers1);
        System.out.println("integers2 before sort: " + integers2);
        Collections.sort(integers2);
        System.out.println("integers2 after sort = " + integers2);
        System.out.println("integers1 size = " + integers1.size() + ", integers2 size = " + integers2.size());
        Collections.copy(integers2, integers1);
        System.out.println("integers2 after copping integers1: " + integers2);
        Collections.addAll(integers2, 23, 3, 53, 53, 36, 19383, 383474747, 35);
        System.out.println("integers2 after adding group of ints: " + integers2);
        Collections.sort(integers2 , (a,b) -> a<b?-1:a==b?0:1);
        System.out.println("integers2 after sorting: " + integers2);
    }
}

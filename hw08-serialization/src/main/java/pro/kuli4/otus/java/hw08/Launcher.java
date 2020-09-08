package pro.kuli4.otus.java.hw08;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/*
 * ./gradlew hw08-serialization:clean hw08-serialization:build
 * java -jar hw08-serialization/build/libs/hw08-serialization-0.0.1.jar
 */

public class Launcher {

    public static void main(String[] args) {

        List<String> al = new ArrayList<>();
        al.add(null);
        al.add("Hello");

        List<PartOfRabbit> scl = new ArrayList<>();
        scl.add(new PartOfRabbit());
        scl.add(new PartOfRabbit(2, 27F));

        Rabbit rabbit = Rabbit.builder()
                .i(1)
                .t(2)
                .j(3)
                .y(5)
                .v(10L)
                .d(43.3526532)
                .c('f')
                .s("Hi")
                .ss(new String[]{null, "One"})
                .sss(al)
                .sc(new PartOfRabbit())
                .scl(scl)
                .ai(new int[]{1, 2, 3})
                .build();

        checkSerializeObject(rabbit);
        checkSerializeObject(null);
    }

    public static void checkSerializeObject(Rabbit rabbit) {
        System.out.println("===========================");

        System.out.println("rabbit: " + rabbit);

        MyGson myGson = new MyGson();
        String str = myGson.toJson(rabbit);

        System.out.println("MyGson: " + str);

        Gson gson = new Gson();
        System.out.println("Gson: " + gson.toJson(rabbit));

        Rabbit rabbit1 = gson.fromJson(str, Rabbit.class);

        if(rabbit != null) {
            System.out.println("Is equals: " + rabbit.equals(rabbit1));
        }
        System.out.println("===========================");
    }
}

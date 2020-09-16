package pro.kuli4.otus.java.hw08;

import pro.kuli4.otus.java.hw08.entities.PartOfRabbit;
import pro.kuli4.otus.java.hw08.entities.Rabbit;

import com.google.gson.Gson;
import pro.kuli4.otus.java.hw08.transform2json.MyGson;

import java.util.ArrayList;
import java.util.Collections;
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

        Gson gson = new Gson();
        MyGson myGson = new MyGson();

        System.out.println(myGson.toJson((byte)1));
        System.out.println(gson.toJson((byte)1));

        System.out.println(myGson.toJson((short)2f));
        System.out.println(gson.toJson((short)2f));

        System.out.println(myGson.toJson(3));
        System.out.println(gson.toJson(3));

        System.out.println(myGson.toJson(4L));
        System.out.println(gson.toJson(4L));

        System.out.println(myGson.toJson(5f));
        System.out.println(gson.toJson(5f));

        System.out.println(myGson.toJson(6d));
        System.out.println(gson.toJson(6d));

        System.out.println(myGson.toJson("aaa"));
        System.out.println(gson.toJson("aaa"));


        System.out.println(myGson.toJson('b'));
        System.out.println(gson.toJson('b'));

        System.out.println(myGson.toJson(new int[] {7, 8, 9}));
        System.out.println(gson.toJson(new int[] {7, 8, 9}));

        System.out.println(myGson.toJson(List.of(10, 11 ,12)));
        System.out.println(gson.toJson(List.of(10, 11 ,12)));

        System.out.println(myGson.toJson(Collections.singletonList(13)));
        System.out.println(gson.toJson(Collections.singletonList(13)));

        boolean b = true;
        System.out.println(myGson.toJson(b));
        System.out.println(gson.toJson(b));

        System.out.println(myGson.toJson(Integer.valueOf(10)));
        System.out.println(gson.toJson(Integer.valueOf(10)));
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

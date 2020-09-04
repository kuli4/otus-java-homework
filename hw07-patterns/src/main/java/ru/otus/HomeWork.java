package ru.otus;

import ru.otus.handler.ComplexProcessor;
import ru.otus.history.HistoryStore;
import ru.otus.history.SimpleHistoryStore;
import ru.otus.listener.HistoryListener;
import ru.otus.listener.Listener;
import ru.otus.processor.ExceptionProcessor;
import ru.otus.processor.Processor;
import ru.otus.processor.ProcessorSwapFields11and13;
import ru.otus.processor.ProcessorUpperField10;

import java.util.List;

public class HomeWork {

    /*
     * For start:
     * ./gradlew :hw07-patterns:clean build
     * java -jar ./hw07-patterns/build/libs/hw07-patterns-0.0.1.jar
     */

    /*
     Реализовать to do:
       1. Добавить поля field11 - field13
       2. Сделать процессор, который поменяет местами значения field11 и field13
       3. Сделать процессор, который будет выбрасывать исключение в четную секунду
       4. Сделать Listener для ведения истории: старое сообщение - новое
     */

    public static void main(String[] args) {
        /*
           по аналогии с Demo.class
           из элеменов "to do" создать new ComplexProcessor и обработать сообщение
         */
        List<Processor> processors = List.of(new ProcessorSwapFields11and13(),
                new ExceptionProcessor(new ProcessorUpperField10()));
        var complexProcessor = new ComplexProcessor(processors, Throwable::printStackTrace);
        HistoryStore hs = new SimpleHistoryStore();
        Listener historyListener = new HistoryListener(hs);
        complexProcessor.addListener(historyListener);

        var message = new Message.Builder()
                .field1("field1")
                .field2("field2")
                .field3("field3")
                .field6("field6")
                .field10("field10")
                .field11("field11")
                .field13("field13")
                .build();

        var result = complexProcessor.handle(message);
        System.out.println("result:" + result);
        System.out.println(hs.getLast());
        complexProcessor.removeListener(historyListener);
    }
}

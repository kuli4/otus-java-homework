package pro.kuli4.otus.java.hw15;

import lombok.extern.slf4j.Slf4j;

/*
 * ./gradlew hw15-concurrency:clean hw15-concurrency:build
 * java -jar hw15-concurrency/build/libs/hw15-concurrency-0.0.1.jar
 */

@Slf4j
public class Launcher {

    public static void main(String[] args) {
        Printer printer = new Printer();
        Thread first = new Thread(new Counter(printer));
        Thread second = new Thread(new Counter(printer));

        first.setName("First");
        second.setName("Second");

        first.start();
        second.start();
    }

    static class Printer {
        public String rightThread = "First";

        public synchronized void print(String message) throws InterruptedException {
            while (!Thread.currentThread().getName().equals(rightThread)) {
                wait();
            }
            Thread.sleep(300);
            rightThread = rightThread.equals("First") ? "Second" : "First";
            log.info("{} prints {}", Thread.currentThread().getName(), message);
            notifyAll();
        }
    }


    static class Counter implements Runnable {

        private final Printer printer;

        Counter(Printer printer) {
            this.printer = printer;
        }

        @Override
        public void run() {
            try {
                while (true) {
                    for (int i = 1; i <= 10; i++) {
                        printer.print(String.valueOf(i));
                    }
                    for (int i = 9; i > 1; i--) {
                        printer.print(String.valueOf(i));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}

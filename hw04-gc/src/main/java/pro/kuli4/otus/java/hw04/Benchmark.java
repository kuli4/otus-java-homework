package pro.kuli4.otus.java.hw04;

import java.util.ArrayList;
import java.util.List;

public class Benchmark {

    private final BenchmarkLogger logger;

    Benchmark(BenchmarkLogger logger) {
        this.logger = logger;
    }

    public void run(int limit) {
        List<Unit> list = new ArrayList<>();
        while (true) {
            long start = System.currentTimeMillis(); // Request start
            for (int i = 0; i < limit * 2; i++) {
                list.add(new Unit(i));
            }
            for (int j = limit * 2; j > 0; j = j - 2) {
                list.remove(j - 1);
            }
            long end = System.currentTimeMillis(); // Request finish
            logger.writeDuration(end, end - start, list.size());
        }
    }


    private static class Unit {
        private final String name;
        private final int[] array = new int[10];

        Unit(int i) {
            this.name = i + "n";
        }

        public String toString() {
            return this.name;
        }
    }
}
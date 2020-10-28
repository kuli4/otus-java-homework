package pro.kuli4.otus.java.hw04;


/*
 * #1
 * SerialGC
 * -XX:+UseSerialGC -Xms256m -Xmx256m
 *
 * #2
 * ParallelGC
 * -XX:+UseParallelGC -Xms256m -Xmx256m
 *
 * #3
 * G1GC
 * -XX:+UseG1GC -Xms256m -Xmx256m
 *
 */

import javax.management.NotificationEmitter;
import javax.management.NotificationListener;
import java.lang.management.GarbageCollectorMXBean;
import java.util.List;

public class Launcher {
    public static void main(String[] args) throws Exception {
        BenchmarkLogger benchmarkLogger = new BenchmarkLogger();
        try {
            switchOnMonitoring(benchmarkLogger);
            Benchmark benchmark = new Benchmark(benchmarkLogger);
            benchmark.run(10000);
        } finally {
            benchmarkLogger.printResult();
            benchmarkLogger.writeStats("G1GC");
        }
    }

    private static void switchOnMonitoring(NotificationListener listener) {
        List<GarbageCollectorMXBean> gcbeans = java.lang.management.ManagementFactory.getGarbageCollectorMXBeans();
        for (GarbageCollectorMXBean gcbean : gcbeans) {
            System.out.println("GC name:" + gcbean.getName());
            NotificationEmitter emitter = (NotificationEmitter) gcbean;
            emitter.addNotificationListener(listener, null, null);
        }
    }

}
package pro.kuli4.otus.java.hw04;

import com.sun.management.GarbageCollectionNotificationInfo;

import javax.management.Notification;
import javax.management.NotificationListener;
import javax.management.openmbean.CompositeData;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class BenchmarkLogger implements NotificationListener {

    private long totalTimeYoung;
    private int countOfYoung;
    private long totalTimeOld;
    private int countOfOld;
    private final List<long[]> durations = new ArrayList<>();
    private final long startTime = System.currentTimeMillis();

    @Override
    public void handleNotification(Notification notification, Object handback) {
        if (notification.getType().equals(GarbageCollectionNotificationInfo.GARBAGE_COLLECTION_NOTIFICATION)) {
            GarbageCollectionNotificationInfo info = GarbageCollectionNotificationInfo.from((CompositeData) notification.getUserData());
            String gcName = info.getGcName();
            String gcAction = info.getGcAction();
            String gcCause = info.getGcCause();

            long startGCTime = System.currentTimeMillis();
            long duration = info.getGcInfo().getDuration();

            if (gcAction.toLowerCase().contains("young") || gcAction.toLowerCase().contains("minor")) {
                totalTimeYoung += duration;
                countOfYoung++;
            } else if (gcAction.toLowerCase().contains("old") || gcAction.toLowerCase().contains("major")) {
                totalTimeOld += duration;
                countOfOld++;
            }

            System.out.println("start:" + (startGCTime - startTime) + " Name:" + gcName + ", action:" + gcAction + ", gcCause:" + gcCause + "(" + duration + " ms)");
        }
    }

    public void writeDuration(long time, long duration, long listSize) {
        durations.add(new long[]{time - startTime, duration, Runtime.getRuntime().freeMemory(), listSize});
    }

    public void printResult() {
        System.out.println("Count of young collections: " + countOfYoung + " with summary duration = " + totalTimeYoung);
        System.out.println("Count of old collections: " + countOfOld + " with summary duration = " + totalTimeOld);
    }

    public void writeStats(String gcName) {
        LocalDateTime time = LocalDateTime.now();
        String durationsFile = gcName + "_durations_" + time.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) + ".csv";
        try (FileWriter writer = new FileWriter(durationsFile)) {
            for (long[] array : durations) {
                writer.write(array[0] + "," + array[1] + "," + array[2] + "," + array[3] + System.lineSeparator());
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}

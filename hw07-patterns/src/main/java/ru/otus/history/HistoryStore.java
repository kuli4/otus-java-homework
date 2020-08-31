package ru.otus.history;

public interface HistoryStore {
    HistoryRecord getLast();
    boolean addRecord(HistoryRecord record);
}

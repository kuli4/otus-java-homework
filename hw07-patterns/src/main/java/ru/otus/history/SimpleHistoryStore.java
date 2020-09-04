package ru.otus.history;

import ru.otus.listener.HistoryListener;

import java.util.ArrayList;
import java.util.List;

public class SimpleHistoryStore implements HistoryStore {

    //lazy init
    private List<HistoryRecord> history;
    private boolean isInit;

    @Override
    public HistoryRecord getLast() {
        return history.get(history.size() - 1);
    }

    @Override
    public boolean addRecord(HistoryRecord record) {
        if (isInit) {
            history.add(record);
        } else {
            history = new ArrayList<>(List.of(record));
        }
        return true;
    }

}

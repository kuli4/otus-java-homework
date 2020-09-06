package ru.otus.listener;

import ru.otus.Message;
import ru.otus.history.HistoryRecord;
import ru.otus.history.HistoryStore;

import java.time.LocalDateTime;

public class HistoryListener implements Listener {

    private final HistoryStore historyStore;

    public HistoryListener(HistoryStore historyStore) {
        this.historyStore = historyStore;
    }

    @Override
    public void onUpdated(Message oldMsg, Message newMsg) {
        historyStore.addRecord(new HistoryRecord(oldMsg, newMsg, LocalDateTime.now()));
    }


}

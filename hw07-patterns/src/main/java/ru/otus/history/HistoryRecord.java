package ru.otus.history;

import ru.otus.Message;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class HistoryRecord {
    private final Message oldMsg;
    private final Message newMsg;
    private final LocalDateTime dateTime;

    public HistoryRecord(Message oldMsg, Message newMsg, LocalDateTime dateTime) {
        this.oldMsg = oldMsg;
        this.newMsg = newMsg;
        this.dateTime = dateTime;
    }

    public Message getOldMsg() {
        return oldMsg;
    }

    public Message getNewMsg() {
        return newMsg;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    @Override
    public String toString() {
        return "DateTime: " + dateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) + System.lineSeparator()+ "Old Msg: " + oldMsg + System.lineSeparator() + "New Msg: " + newMsg;
    }
}

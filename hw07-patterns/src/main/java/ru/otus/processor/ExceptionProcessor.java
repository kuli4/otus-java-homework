package ru.otus.processor;

import ru.otus.Message;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ExceptionProcessor implements Processor {
    private final Processor processor;

    public ExceptionProcessor(Processor processor) {
        this.processor = processor;
    }

    @Override
    public Message process(Message message) {
        LocalDateTime ldt = LocalDateTime.now();
        if (ldt.getSecond() % 2 == 0) {
            throw new RuntimeException("An even second appeared! Time: " + ldt.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        }
        return processor.process(message);
    }
}

package com.testcontainers.messages.domain;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Repository;

@Repository
public class MessageRepository {
    private static final AtomicLong ID = new AtomicLong(0L);
    private static final List<Message> MESSAGES = new ArrayList<>();

    public List<Message> getMessages() {
        return List.copyOf(MESSAGES);
    }

    public Message createMessage(Message message) {
        MESSAGES.add(new Message(ID.incrementAndGet(), message.content(), message.createdBy(), Instant.now()));
        return message;
    }
}

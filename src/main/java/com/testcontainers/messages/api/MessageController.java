package com.testcontainers.messages.api;

import com.testcontainers.messages.domain.Message;
import com.testcontainers.messages.domain.MessageRepository;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/messages")
class MessageController {

    private final MessageRepository messageRepository;

    MessageController(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @GetMapping
    List<Message> getMessages() {
        return messageRepository.getMessages();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    Message createMessage(@RequestBody @Valid Message message) {
        return messageRepository.createMessage(message);
    }
}

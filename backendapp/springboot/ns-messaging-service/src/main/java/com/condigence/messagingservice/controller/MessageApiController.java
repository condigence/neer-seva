package com.condigence.messagingservice.controller;

import com.condigence.messagingservice.entity.MessageEntity;
import com.condigence.messagingservice.payload.MessageRequest;
import com.condigence.messagingservice.service.MessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/messages")
public class MessageApiController {

    private final MessageService messageService;

    public MessageApiController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping
    public ResponseEntity<MessageEntity> publish(@RequestBody MessageRequest request) {
        MessageEntity saved = messageService.publishAndSave(request);
        return ResponseEntity.ok(saved);
    }

    @GetMapping
    public ResponseEntity<List<MessageEntity>> list() {
        return ResponseEntity.ok(messageService.listAll());
    }
}


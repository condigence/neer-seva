package com.condigence.messagingservice.service;

import com.condigence.messagingservice.config.RabbitMQConfig;
import com.condigence.messagingservice.entity.MessageEntity;
import com.condigence.messagingservice.payload.MessageRequest;
import com.condigence.messagingservice.repo.MessageRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MessageService {

    private final RabbitTemplate rabbitTemplate;
    private final MessageRepository messageRepository;

    public MessageService(RabbitTemplate rabbitTemplate, MessageRepository messageRepository) {
        this.rabbitTemplate = rabbitTemplate;
        this.messageRepository = messageRepository;
    }

    @Transactional
    public MessageEntity publishAndSave(MessageRequest request) {
        // save first (sender side)
        MessageEntity entity = new MessageEntity(request.getContent());
        MessageEntity saved = messageRepository.save(entity);

        // publish to RabbitMQ (JSON)
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE, RabbitMQConfig.ROUTING_KEY, request);

        return saved;
    }

    public List<MessageEntity> listAll() {
        return messageRepository.findAll();
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE)
    public void handleMessage(MessageRequest request) {
        // consumer side: persist received message
        MessageEntity entity = new MessageEntity(request.getContent());
        messageRepository.save(entity);
    }
}


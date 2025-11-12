package com.condigence.nsnotificationservice.service;

import com.condigence.nsnotificationservice.dto.MessageDTO;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    @KafkaListener(topics = "ns-notification-topic", groupId = "ns-notification-group", containerFactory = "kafkaListenerContainerFactory")
    public void listen(MessageDTO message) {
        // Simple consumer that logs the message. In a real app, this could call an SMS provider API, persist a record, etc.
        System.out.println("[Kafka Consumer] Received message for toNumber=" + message.getToNumber() + ", text=" + message.getMessage());

        // Here you could call an SMS provider (HTTP API) to deliver the message.
    }
}

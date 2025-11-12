package com.condigence.nsnotificationservice.service;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.condigence.nsnotificationservice.config.KafkaProperties;

@Service
public class KafkaProducerService {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final String topic;

    public KafkaProducerService(KafkaTemplate<String, Object> kafkaTemplate, KafkaProperties kafkaProperties) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = kafkaProperties.getTopic();
    }

    public void sendMessage(String key, Object payload) {
        kafkaTemplate.send(topic, key, payload);
    }
}

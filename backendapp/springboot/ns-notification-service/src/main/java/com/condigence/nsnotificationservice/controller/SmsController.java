package com.condigence.nsnotificationservice.controller;

import com.condigence.nsnotificationservice.dto.SmsDTO;
import com.condigence.nsnotificationservice.dto.MessageDTO;
import com.condigence.nsnotificationservice.service.KafkaProducerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("neerseva/api/v1/notifications")
public class SmsController {

    private final KafkaProducerService producerService;

    public SmsController(KafkaProducerService producerService) {
        this.producerService = producerService;
    }

    @GetMapping(value = "/")
    public ResponseEntity<String> hello() {
        return new ResponseEntity<>("Hello from NS Notification Service!", HttpStatus.OK);

    }

    @GetMapping(value = "/sendSMS")
    public ResponseEntity<String> sendSMS() {
        return new ResponseEntity<>("Message sent successfully", HttpStatus.OK);
    }


    @PostMapping(value = "/sendSMS")
    public ResponseEntity<String> sendSMS(@RequestBody SmsDTO smsDTO) {
        // Publish the SMS request to Kafka for asynchronous delivery by consumer
        MessageDTO messageDTO = new MessageDTO(null, smsDTO.getToNumber(), smsDTO.getFromNumber(), smsDTO.getMessage());
        producerService.sendMessage(String.valueOf(System.currentTimeMillis()), messageDTO);
        return new ResponseEntity<>("Queued for sending", HttpStatus.ACCEPTED);
    }

    // Endpoint to publish the message to Kafka topic (kept for direct MessageDTO publish)
    @PostMapping(value = "/publish")
    public ResponseEntity<String> publishToKafka(@RequestBody MessageDTO messageDTO) {
        producerService.sendMessage(String.valueOf(messageDTO.getId() != null ? messageDTO.getId() : System.currentTimeMillis()), messageDTO);
        return new ResponseEntity<>("Published to Kafka", HttpStatus.ACCEPTED);
    }
}

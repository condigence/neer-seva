package com.condigence.nsnotificationservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.condigence.nsnotificationservice.config.KafkaProperties;


@SpringBootApplication
@EnableConfigurationProperties(KafkaProperties.class)
public class NsNotificationServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(NsNotificationServiceApplication.class, args);
	}

}

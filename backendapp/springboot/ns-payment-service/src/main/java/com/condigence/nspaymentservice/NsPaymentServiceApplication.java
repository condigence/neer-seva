package com.condigence.nspaymentservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class NsPaymentServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(NsPaymentServiceApplication.class, args);
	}

}

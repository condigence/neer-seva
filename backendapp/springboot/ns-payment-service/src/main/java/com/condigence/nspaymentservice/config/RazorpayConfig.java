package com.condigence.nspaymentservice.config;

import com.razorpay.RazorpayClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RazorpayConfig {

    @Bean
    public RazorpayClient razorpayClient() {
        // Use local stub; in production replace with official SDK and credentials
        return new RazorpayClient();
    }
}

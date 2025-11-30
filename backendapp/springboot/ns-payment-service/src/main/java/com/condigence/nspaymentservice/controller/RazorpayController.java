package com.condigence.nspaymentservice.controller;

import com.condigence.nspaymentservice.entity.OrderPayment;
import com.condigence.nspaymentservice.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class RazorpayController {

    @Autowired
    private PaymentService paymentService;

    // Create an order (demo)
    @PostMapping("/createOrder")
    public ResponseEntity<?> createOrder(@RequestParam double amount, @RequestParam(required = false, defaultValue = "UPI") String method) {
        OrderPayment order = paymentService.createOrder(amount, method);
        return ResponseEntity.ok(Map.of("orderId", order.getOrder_id(), "amount", order.getAmount()));
    }

    // Mark order paid (demo)
    @PostMapping("/markPaid")
    public ResponseEntity<?> markPaid(@RequestParam String orderId, @RequestParam String paymentId) {
        paymentService.markPaid(orderId, paymentId);
        return ResponseEntity.ok(Map.of("orderId", orderId, "status", "PAID"));
    }
}

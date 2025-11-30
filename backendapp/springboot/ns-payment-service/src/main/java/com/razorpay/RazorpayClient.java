package com.razorpay;

import java.util.HashMap;
import java.util.Map;

// Simple local stub for RazorpayClient to allow compile-time wiring. Replace with official SDK for production.
public class RazorpayClient {

    public RazorpayClient() {
    }

    public Map<String, Object> ordersCreate(Map<String, Object> payload) {
        Map<String, Object> resp = new HashMap<>();
        resp.put("id", "rzp_test_" + System.currentTimeMillis());
        resp.put("amount", payload.get("amount"));
        return resp;
    }
}


package com.condigence.nspaymentservice.service;

import com.condigence.nspaymentservice.entity.OrderPayment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class PaymentService {

    private final Map<String, OrderPayment> store = new HashMap<>();
    private final AtomicLong idGen = new AtomicLong(1000);

    @Autowired(required = false)
    private com.razorpay.RazorpayClient razorpayClient;

    public OrderPayment createOrder(double amount, String paymentMethod) {
        OrderPayment o = new OrderPayment();
        o.setOrderPaymentId(idGen.incrementAndGet());
        o.setAmount(amount);
        o.setPaymentMethod(paymentMethod);
        o.setStatus("PENDING");
        String orderId = "ORD" + o.getOrderPaymentId();
        o.setOrder_id(orderId);
        store.put(orderId, o);
        return o;
    }

    public Map<String, Object> createRazorpayOrder(double amount) throws Exception {
        int amtInPaise = (int) (amount * 100);
        Map<String, Object> payload = new HashMap<>();
        payload.put("amount", amtInPaise);
        payload.put("currency", "INR");
        payload.put("receipt", "neerseva_" + System.currentTimeMillis());

        Map<String, Object> resp;
        if (razorpayClient != null) {
            resp = razorpayClient.ordersCreate(payload);
        } else {
            // Fallback fake response
            resp = new HashMap<>();
            resp.put("id", "rzp_test_" + System.currentTimeMillis());
            resp.put("amount", amtInPaise);
        }

        // create local order record
        OrderPayment o = createOrder(amount, "RAZORPAY");
        o.setRazorpayOrderId((String) resp.get("id"));
        store.put(o.getOrder_id(), o);

        Map<String, Object> result = new HashMap<>();
        result.put("orderId", o.getOrder_id());
        result.put("razorpayOrderId", resp.get("id"));
        result.put("amount", resp.get("amount"));
        return result;
    }

    public void markPaid(String orderId, String razorpayPaymentId) {
        OrderPayment o = store.get(orderId);
        if (o != null) {
            o.setRazorpayPaymentId(razorpayPaymentId);
            o.setStatus("PAID");
        }
    }

}

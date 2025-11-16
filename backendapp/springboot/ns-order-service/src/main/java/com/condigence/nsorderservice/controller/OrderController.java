package com.condigence.nsorderservice.controller;


import com.condigence.nsorderservice.dto.OrderDTO;
import com.condigence.nsorderservice.dto.OrderDetailDTO;
import com.condigence.nsorderservice.entity.Order;
import com.condigence.nsorderservice.service.OrderService;
import com.condigence.nsorderservice.util.CustomErrorType;
import com.condigence.nsorderservice.exception.BadRequestException;
import com.condigence.nsorderservice.exception.ResourceNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/neerseva/api/v1/orders")
public class OrderController {

    public static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    OrderService orderService;

    // AppProperties isn't used in this controller currently; keep wiring in service if needed.

    @PostMapping(value = "/")
    public ResponseEntity<?> placeOrder(@RequestBody OrderDetailDTO orderdetailDTO) {
        logger.info("Entering placeOrder with Order Details >>>>>>>>  : {}", orderdetailDTO);
        HttpHeaders headers = new HttpHeaders();
        try {
            boolean saved = orderService.saveOrderDetail(orderdetailDTO);
            if (!saved) {
                return new ResponseEntity<>(new CustomErrorType("Unable to save order. Check shop id and payload."), HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(headers, HttpStatus.CREATED);
        } catch (ResourceNotFoundException rnfe) {
            logger.warn("placeOrder - resource not found: {}", rnfe.getMessage());
            String userMsg = extractMessageFromResourceNotFound(rnfe.getMessage());
            return new ResponseEntity<>(new CustomErrorType(userMsg), HttpStatus.NOT_FOUND);
        } catch (BadRequestException bre) {
            logger.warn("placeOrder - bad request: {}", bre.getMessage());
            return new ResponseEntity<>(new CustomErrorType(bre.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            logger.error("placeOrder - unexpected error: {}", e.getMessage());
            return new ResponseEntity<>(new CustomErrorType("Internal server error"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/")
    public ResponseEntity<?> getAllOrders() {
        return ResponseEntity.status(HttpStatus.OK).body(orderService.getAllOrders());
    }

    @GetMapping("/byCustomer/{id}")
    public ResponseEntity<?> getOrderByCustomerId(@PathVariable("id") Long id) {
        logger.info("inside getOrderByCustomerId with id {}", id);
        if (id == null) {
            return new ResponseEntity<>(new CustomErrorType("Invalid customer id"), HttpStatus.BAD_REQUEST);
        }
        List<OrderDTO> customerOrders = orderService.getOrderBycustomerId(id.longValue());
        if (customerOrders != null && !customerOrders.isEmpty()) {
            return ResponseEntity.ok(customerOrders);
        }
        return new ResponseEntity<>(new CustomErrorType("Order not found."), HttpStatus.NOT_FOUND);
    }

    ////////////////////////////////////// ORDERS////////////////////////////////////////

    @GetMapping("/v1/orders/customer/{id}")
    public ResponseEntity<?> getOrderByCustomerIdV1(@PathVariable("id") Integer custId) {
        logger.info("getOrderByCustomerIdV1 with id {}", custId);
        if (custId == null) {
            return new ResponseEntity<>(new CustomErrorType("Invalid customer id"), HttpStatus.BAD_REQUEST);
        }
        List<OrderDTO> orderList = orderService.getOrderBycustomerId(custId.longValue());
        return ResponseEntity.ok(orderList);
    }

    @PostMapping(value = "/v1/orders/place")
    public ResponseEntity<?> placeOrderItems(@RequestBody OrderDetailDTO orderdetailDTO) {
        logger.info("placeOrderItems detail is>>>>>>>>  : {}", orderdetailDTO);
        HttpHeaders headers = new HttpHeaders();
        try {
            boolean saved = orderService.saveOrderDetail(orderdetailDTO);
            if (!saved) {
                return new ResponseEntity<>(new CustomErrorType("Unable to save order. Check shop id and payload."), HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(headers, HttpStatus.CREATED);
        } catch (ResourceNotFoundException rnfe) {
            logger.warn("placeOrderItems - resource not found: {}", rnfe.getMessage());
            String userMsg = extractMessageFromResourceNotFound(rnfe.getMessage());
            return new ResponseEntity<>(new CustomErrorType(userMsg), HttpStatus.NOT_FOUND);
        } catch (BadRequestException bre) {
            logger.warn("placeOrderItems - bad request: {}", bre.getMessage());
            return new ResponseEntity<>(new CustomErrorType(bre.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            logger.error("placeOrderItems - unexpected error: {}", e.getMessage());
            return new ResponseEntity<>(new CustomErrorType("Internal server error"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/v1/orders")
    public ResponseEntity<List<Order>> getAll() {
        List<Order> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/to/vendor/{id}")
    public ResponseEntity<?> getOrderByVendorId(@PathVariable("id") Integer vendorId) {
        logger.info("getOrderByVendorId with id {}", vendorId);
        if (vendorId == null) {
            return new ResponseEntity<>(new CustomErrorType("Invalid vendor id"), HttpStatus.BAD_REQUEST);
        }
        List<OrderDTO> orderList = orderService.getOrderByVendorId(vendorId.longValue());
        return ResponseEntity.ok(orderList);
    }

    @PatchMapping("/v1/orders/status/update/{id}")
    public ResponseEntity<?> updateOrderStatus(@RequestBody OrderDTO dto, @PathVariable("id") Integer id) {
        if (id == null) return new ResponseEntity<>(new CustomErrorType("Invalid order id"), HttpStatus.BAD_REQUEST);
        OrderDTO newOrder = orderService.save(dto, id);
        return ResponseEntity.ok(newOrder);
    }

    //////////////////////////////////////////////////////////////////////////////////////////
//	private ImageDTO getPicById(long imageId) {
//		String imagePath = app.getLocation();
//		return imageService.getImage(imageId, imagePath);
//	}

///////////////////////////////////////////////////////////////////
    // Try to extract inner JSON error.message from exception message if present
    private String extractMessageFromResourceNotFound(String fullMessage) {
        if (fullMessage == null) return "Resource not found";
        int jsonStart = fullMessage.indexOf('{');
        if (jsonStart >= 0) {
            String json = fullMessage.substring(jsonStart);
            try {
                ObjectMapper mapper = new ObjectMapper();
                @SuppressWarnings("unchecked")
                java.util.Map<String, Object> m = mapper.readValue(json, java.util.Map.class);
                if (m == null) return fullMessage;
                Object em = m.get("errorMessage");
                if (em == null) em = m.get("message");
                if (em == null) em = m.get("error");
                if (em != null) return em.toString();
                // fallback: return the original JSON string
                return json;
            } catch (Exception ex) {
                logger.debug("extractMessageFromResourceNotFound: failed to parse JSON from message: {}", ex.getMessage());
                return fullMessage;
            }
        }
        // No JSON found, return as-is
        return fullMessage;
    }
}

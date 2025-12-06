package com.condigence.nsorderservice.controller;


import com.condigence.nsorderservice.dto.OrderDTO;
import com.condigence.nsorderservice.dto.OrderDetailDTO;
import com.condigence.nsorderservice.entity.Order;
import com.condigence.nsorderservice.service.OrderService;
import com.condigence.nsorderservice.util.CustomErrorType;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Orders", description = "Order management APIs")
public class OrderController {

    public static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    OrderService orderService;

    @Operation(summary = "Place a new order (legacy endpoint)", description = "Creates an order and triggers stock update.")
    @ApiResponse(responseCode = "201", description = "Order created successfully")
    @ApiResponse(responseCode = "400", description = "Invalid request or stock update failure",
            content = @Content(schema = @Schema(implementation = CustomErrorType.class)))
    @PostMapping(value = "/")
    public ResponseEntity<?> placeOrder(@RequestBody OrderDetailDTO orderdetailDTO) {
        logger.info("Entering placeOrder with Order Details >>>>>>>>  : {}", orderdetailDTO);
        HttpHeaders headers = new HttpHeaders();
        boolean saved = orderService.saveOrderDetail(orderdetailDTO);
        if (!saved) {
            return new ResponseEntity<>(new CustomErrorType("Unable to save order. Check shop id and payload."), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @Operation(summary = "Get all orders", description = "Returns a list of all orders.")
    @ApiResponse(responseCode = "200", description = "List of orders returned successfully")
    @GetMapping("/")
    public ResponseEntity<?> getAllOrders() {
        return ResponseEntity.status(HttpStatus.OK).body(orderService.getAllOrders());
    }

    @Operation(summary = "Get orders by customer id", description = "Returns orders for a specific customer.")
    @ApiResponse(responseCode = "200", description = "Customer orders returned successfully")
    @ApiResponse(responseCode = "400", description = "Invalid customer id",
            content = @Content(schema = @Schema(implementation = CustomErrorType.class)))
    @ApiResponse(responseCode = "404", description = "No orders found for customer",
            content = @Content(schema = @Schema(implementation = CustomErrorType.class)))
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

    @Operation(summary = "Get orders by customer id (v1)", description = "Returns orders for a specific customer using v1 endpoint.")
    @ApiResponse(responseCode = "200", description = "Customer orders returned successfully")
    @ApiResponse(responseCode = "400", description = "Invalid customer id",
            content = @Content(schema = @Schema(implementation = CustomErrorType.class)))
    @GetMapping("/v1/orders/customer/{id}")
    public ResponseEntity<?> getOrderByCustomerIdV1(@PathVariable("id") Integer custId) {
        logger.info("getOrderByCustomerIdV1 with id {}", custId);
        if (custId == null) {
            return new ResponseEntity<>(new CustomErrorType("Invalid customer id"), HttpStatus.BAD_REQUEST);
        }
        List<OrderDTO> orderList = orderService.getOrderBycustomerId(custId.longValue());
        return ResponseEntity.ok(orderList);
    }

    @Operation(summary = "Place a new order (v1)", description = "Creates an order and triggers stock update using v1 endpoint.")
    @ApiResponse(responseCode = "201", description = "Order created successfully")
    @ApiResponse(responseCode = "400", description = "Invalid request or stock update failure",
            content = @Content(schema = @Schema(implementation = CustomErrorType.class)))
    @PostMapping(value = "/v1/orders/place")
    public ResponseEntity<?> placeOrderItems(@RequestBody OrderDetailDTO orderdetailDTO) {
        logger.info("placeOrderItems detail is>>>>>>>>  : {}", orderdetailDTO);
        HttpHeaders headers = new HttpHeaders();
        boolean saved = orderService.saveOrderDetail(orderdetailDTO);
        if (!saved) {
            return new ResponseEntity<>(new CustomErrorType("Unable to save order. Check shop id and payload."), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @Operation(summary = "Get total order count", description = "Returns the count of all orders.")
    @ApiResponse(responseCode = "200", description = "Order count returned successfully")
    @GetMapping("/count")
    public ResponseEntity<Integer> getAll() {
        List<Order> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders.size());
    }

    @Operation(summary = "Get orders by vendor id", description = "Returns orders assigned to a specific vendor.")
    @ApiResponse(responseCode = "200", description = "Vendor orders returned successfully")
    @ApiResponse(responseCode = "400", description = "Invalid vendor id",
            content = @Content(schema = @Schema(implementation = CustomErrorType.class)))
    @GetMapping("/to/vendor/{id}")
    public ResponseEntity<?> getOrderByVendorId(@PathVariable("id") Integer vendorId) {
        logger.info("getOrderByVendorId with id {}", vendorId);
        if (vendorId == null) {
            return new ResponseEntity<>(new CustomErrorType("Invalid vendor id"), HttpStatus.BAD_REQUEST);
        }
        List<OrderDTO> orderList = orderService.getOrderByVendorId(vendorId.longValue());
        return ResponseEntity.ok(orderList);
    }

    @Operation(summary = "Update order delivery status", description = "Updates the delivery status for a specific order.")
    @ApiResponse(responseCode = "200", description = "Order status updated successfully")
    @ApiResponse(responseCode = "400", description = "Invalid order id",
            content = @Content(schema = @Schema(implementation = CustomErrorType.class)))
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

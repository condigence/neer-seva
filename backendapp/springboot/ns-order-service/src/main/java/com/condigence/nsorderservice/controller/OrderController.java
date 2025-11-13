package com.condigence.nsorderservice.controller;


import com.condigence.nsorderservice.dto.OrderDTO;
import com.condigence.nsorderservice.dto.OrderDetailDTO;
import com.condigence.nsorderservice.entity.Order;
import com.condigence.nsorderservice.service.OrderService;
import com.condigence.nsorderservice.util.AppProperties;
import com.condigence.nsorderservice.util.CustomErrorType;
import com.condigence.nsorderservice.exception.BadRequestException;
import com.condigence.nsorderservice.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/neerseva/api/v1/orders")
public class OrderController {

    public static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    OrderService orderService;

    @Autowired
    public void setApp(AppProperties app) {
        this.app = app;
    }

    private AppProperties app;

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
            return new ResponseEntity<>(new CustomErrorType(rnfe.getMessage()), HttpStatus.NOT_FOUND);
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
        System.out.println("inside getOrderByCustomerId!");

        List<OrderDTO> customerOrders = orderService.getOrderBycustomerId(id);


        if (customerOrders.size() > 1) {
            return ResponseEntity.status(HttpStatus.OK).body(customerOrders);

        } else {
            return new ResponseEntity(new CustomErrorType("Order not found."), HttpStatus.NOT_FOUND);
        }


    }

    ////////////////////////////////////// ORDERS////////////////////////////////////////

    @GetMapping("/v1/orders/customer/{id}")
    public ResponseEntity<?> getOrderByCustomerId(@PathVariable("id") Integer custId) {
        logger.info("getOrderByCustomerId with id {}", custId);
        List<OrderDTO> orderList = orderService.getOrderBycustomerId(custId);
        return new ResponseEntity<List<OrderDTO>>(orderList, HttpStatus.OK);

    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @PostMapping(value = "/v1/orders/place")
    public ResponseEntity<?> placeOrderItems(@RequestBody OrderDetailDTO orderdetailDTO) {
        logger.info("placeOrderItems detail is>>>>>>>>  : {}", orderdetailDTO);
        HttpHeaders headers = new HttpHeaders();
        try {
            boolean orderDetail = orderService.saveOrderDetail(orderdetailDTO);
            if (!orderDetail) {
                return new ResponseEntity(new CustomErrorType("Unable to save order. Check shop id and payload."), HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<String>(headers, HttpStatus.CREATED);
        } catch (ResourceNotFoundException rnfe) {
            logger.warn("placeOrderItems - resource not found: {}", rnfe.getMessage());
            return new ResponseEntity<>(new CustomErrorType(rnfe.getMessage()), HttpStatus.NOT_FOUND);
        } catch (BadRequestException bre) {
            logger.warn("placeOrderItems - bad request: {}", bre.getMessage());
            return new ResponseEntity<>(new CustomErrorType(bre.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            logger.error("placeOrderItems - unexpected error: {}", e.getMessage());
            return new ResponseEntity<>(new CustomErrorType("Internal server error"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/v1/orders")
    public ResponseEntity<?> getAll() {
        List<Order> orders = new ArrayList<>();
        orders = orderService.getAllOrders();
        return ResponseEntity.status(HttpStatus.OK).body(orders);
    }

    @GetMapping("/to/vendor/{id}")
    public ResponseEntity<?> getOrderByVendorId(@PathVariable("id") Integer vendorId) {
        logger.info("getOrderByVendorId with id {}", vendorId);
        List<OrderDTO> orderList = orderService.getOrderByVendorId(vendorId);
        return new ResponseEntity<List<OrderDTO>>(orderList, HttpStatus.OK);

    }

    @PatchMapping("/v1/orders/status/update/{id}")
    public ResponseEntity<?> updateOrderStatus(@RequestBody OrderDTO dto, @PathVariable("id") Integer id) {
        OrderDTO newOrder = orderService.save(dto, id);
        return new ResponseEntity<>(newOrder, HttpStatus.OK);
    }

    //////////////////////////////////////////////////////////////////////////////////////////
//	private ImageDTO getPicById(long imageId) {
//		String imagePath = app.getLocation();
//		return imageService.getImage(imageId, imagePath);
//	}

///////////////////////////////////////////////////////////////////
}

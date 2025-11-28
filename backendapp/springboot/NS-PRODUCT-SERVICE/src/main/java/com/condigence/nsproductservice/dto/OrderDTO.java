package com.condigence.nsproductservice.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderDTO {

    private Long orderId;

    private LocalDate orderDate;

    private LocalTime orderTime;

    private String orderDeliveryStatus;

    private String orderStatus;

    private String eta;

    private Integer orderGrandTotal;

    private OrderDetailDTO orderDetail;

}

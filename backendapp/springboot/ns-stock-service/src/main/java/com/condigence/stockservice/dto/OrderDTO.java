package com.condigence.stockservice.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.Data;

@Data
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

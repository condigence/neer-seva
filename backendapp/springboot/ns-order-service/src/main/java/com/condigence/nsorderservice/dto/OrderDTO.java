package com.condigence.nsorderservice.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
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

package com.condigence.nsproductservice.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public class OrderDTO {

	private Long orderId;

	private LocalDate orderDate;

	private LocalTime orderTime;

	private String orderDeliveryStatus;

	private String orderStatus;

	private String eta;

	private Integer orderGrandTotal;

	private OrderDetailDTO orderDetail;

	public Long getOrderId() {
		return orderId;
	}

	public LocalDate getOrderDate() {
		return orderDate;
	}

	public LocalTime getOrderTime() {
		return orderTime;
	}

	public String getOrderDeliveryStatus() {
		return orderDeliveryStatus;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public String getEta() {
		return eta;
	}

	public OrderDetailDTO getOrderDetail() {
		return orderDetail;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public void setOrderDate(LocalDate orderDate) {
		this.orderDate = orderDate;
	}

	public void setOrderTime(LocalTime orderTime) {
		this.orderTime = orderTime;
	}

	public void setOrderDeliveryStatus(String orderDeliveryStatus) {
		this.orderDeliveryStatus = orderDeliveryStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public void setEta(String eta) {
		this.eta = eta;
	}

	public void setOrderDetail(OrderDetailDTO orderDetail) {
		this.orderDetail = orderDetail;
	}

	public Integer getOrderGrandTotal() {
		return orderGrandTotal;
	}

	public void setOrderGrandTotal(Integer orderGrandTotal) {
		this.orderGrandTotal = orderGrandTotal;
	}

}

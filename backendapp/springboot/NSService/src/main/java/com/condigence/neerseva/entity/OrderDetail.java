package com.condigence.neerseva.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.ToString;

@Entity
@Table(name = "order_detail")
@ToString
public class OrderDetail {

	public OrderDetail() {
		super();
	}

	
	@Id
	@Column(name = "order_detail_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long orderDetailId;

	@Column(name = "order_tem_id")
	private Long orderItemId;

	@Column(name = "order_item_quantity")
	private Integer orderItemQuantity;
	
	
	@Column(name = "order_item_price")
	private Integer orderItemPrice;

	@Column(name = "order_total_amount")
	private Integer orderTotalamount;

	@Column(name = "order_discount")
	private Integer orderDiscount;

	@Column(name = "order_subTotal")
	private Integer orderSubTotal;

	@Column(name = "order_service_charge")
	private Integer orderServiceCharge;

	@Column(name = "order_GST")
	private Integer orderGST;

	public Long getOrderDetailId() {
		return orderDetailId;
	}

	public Long getOrderItemId() {
		return orderItemId;
	}

	public Integer getOrderItemQuantity() {
		return orderItemQuantity;
	}

	public Integer getOrderTotalamount() {
		return orderTotalamount;
	}

	public Integer getOrderDiscount() {
		return orderDiscount;
	}

	public Integer getOrderSubTotal() {
		return orderSubTotal;
	}

	public Integer getOrderServiceCharge() {
		return orderServiceCharge;
	}

	public Integer getOrderGST() {
		return orderGST;
	}

	public void setOrderDetailId(Long orderDetailId) {
		this.orderDetailId = orderDetailId;
	}

	public void setOrderItemId(Long orderItemId) {
		this.orderItemId = orderItemId;
	}

	public void setOrderItemQuantity(Integer orderItemQuantity) {
		this.orderItemQuantity = orderItemQuantity;
	}

	public void setOrderTotalamount(Integer orderTotalamount) {
		this.orderTotalamount = orderTotalamount;
	}

	public void setOrderDiscount(Integer orderDiscount) {
		this.orderDiscount = orderDiscount;
	}

	public void setOrderSubTotal(Integer orderSubTotal) {
		this.orderSubTotal = orderSubTotal;
	}

	public void setOrderServiceCharge(Integer orderServiceCharge) {
		this.orderServiceCharge = orderServiceCharge;
	}

	public void setOrderGST(Integer orderGST) {
		this.orderGST = orderGST;
	}

	public Integer getOrderItemPrice() {
		return orderItemPrice;
	}

	public void setOrderItemPrice(Integer orderItemPrice) {
		this.orderItemPrice = orderItemPrice;
	}

	

}

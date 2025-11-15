package com.condigence.nsorderservice.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "order_detail")
public class OrderDetail {

	public OrderDetail() {
		super();
	}

	
	@Id
	@Column(name = "order_detail_id")
	@SequenceGenerator(name = "order_detail_seq_gen", sequenceName = "order_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_detail_seq_gen")
	private Long orderDetailId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_id")
	private Order order;

	@Column(name = "order_item_id")
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

	public Order getOrder() {
		return order;
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

	public void setOrder(Order order) {
		this.order = order;
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

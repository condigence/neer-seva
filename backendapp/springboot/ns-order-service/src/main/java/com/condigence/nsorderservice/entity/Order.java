package com.condigence.nsorderservice.entity;



import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "cust_order")
public class Order {

	public Order() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long orderId;

	@Column(name = "order_number")
	private String orderNumber;
	
	@OneToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "cust_order_order_detail", joinColumns = @JoinColumn(name = "id"), inverseJoinColumns = @JoinColumn(name = "order_detail_id"))
	private List<OrderDetail> orderDetail;

	@Column(name = "order_date")
	private LocalDate orderDate;

	@Column(name = "order_time")
	private LocalTime orderTime;

	@Column(name = "order_from_cust_id")
	private Long orderFromCustId;

	public Long getOrderFromCustId() {
		return orderFromCustId;
	}

	public void setOrderFromCustId(Long orderFromCustId) {
		this.orderFromCustId = orderFromCustId;
	}

	@Column(name = "order_to_vendor_id")
	private Long orderToVendorId;

	public Long getOrderToShopId() {
		return orderToShopId;
	}

	public void setOrderToShopId(Long orderToShopId) {
		this.orderToShopId = orderToShopId;
	}

	@Column(name = "order_to_shop_id")
	private Long orderToShopId;
	
	@Column(name = "order_grand_total")
	private Long orderGrandTotal;

	@Column(name = "order_status")
	private String orderStatus;

	@Column(name = "order_delivery_status")
	private String orderDeliveryStatus;

	@Column(name = "eta")
	private String eta;

	@Column(name = "order_location_code")
	private String orderLocationCode;

	@Column(name = "orderType")
	private String orderType;

	@Column(name = "order_is_cancelled")
	private String orderIsCancelled;

	@Column(name = "order_is_paid")
	private String orderIsPaid;

	@Column(name = "order_payment_type")
	private String orderPaymentType;

	@Column(name = "order_payment_date")
	private Date orderPaymentDate;

	@Column(name = "order_payment_time")
	private String orderPaymentTime;

	@Column(name = "order_payment_method")
	private String orderPaymentMethod;

	@Column(name = "order_payament_txn_id")
	private String orderpayamentTxnId;

	@Column(name = "order_payment_cust_contact")
	private String orderPaymentCustContact;

	public Long getOrderId() {
		return orderId;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public LocalDate getOrderDate() {
		return orderDate;
	}

	

	

	public Long getOrderToVendorId() {
		return orderToVendorId;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public String getOrderDeliveryStatus() {
		return orderDeliveryStatus;
	}

	public String getEta() {
		return eta;
	}

	public String getOrderLocationCode() {
		return orderLocationCode;
	}

	public String getOrderType() {
		return orderType;
	}

	public String getOrderIsCancelled() {
		return orderIsCancelled;
	}

	public LocalTime getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(LocalTime orderTime) {
		this.orderTime = orderTime;
	}

	public String getOrderIsPaid() {
		return orderIsPaid;
	}

	public String getOrderPaymentType() {
		return orderPaymentType;
	}

	public Long getOrderGrandTotal() {
		return orderGrandTotal;
	}

	public void setOrderGrandTotal(Long orderGrandTotal) {
		this.orderGrandTotal = orderGrandTotal;
	}

	public Date getOrderPaymentDate() {
		return orderPaymentDate;
	}

	public String getOrderPaymentTime() {
		return orderPaymentTime;
	}

	public String getOrderPaymentMethod() {
		return orderPaymentMethod;
	}

	public String getOrderpayamentTxnId() {
		return orderpayamentTxnId;
	}

	public String getOrderPaymentCustContact() {
		return orderPaymentCustContact;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public void setOrderDate(LocalDate orderDate) {
		this.orderDate = orderDate;
	}

	


	public void setOrderToVendorId(Long orderToVendorId) {
		this.orderToVendorId = orderToVendorId;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public void setOrderDeliveryStatus(String orderDeliveryStatus) {
		this.orderDeliveryStatus = orderDeliveryStatus;
	}

	public void setEta(String eta) {
		this.eta = eta;
	}

	public void setOrderLocationCode(String orderLocationCode) {
		this.orderLocationCode = orderLocationCode;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public void setOrderIsCancelled(String orderIsCancelled) {
		this.orderIsCancelled = orderIsCancelled;
	}

	public void setOrderIsPaid(String orderIsPaid) {
		this.orderIsPaid = orderIsPaid;
	}

	public void setOrderPaymentType(String orderPaymentType) {
		this.orderPaymentType = orderPaymentType;
	}

	public void setOrderPaymentDate(Date orderPaymentDate) {
		this.orderPaymentDate = orderPaymentDate;
	}

	public void setOrderPaymentTime(String orderPaymentTime) {
		this.orderPaymentTime = orderPaymentTime;
	}

	public void setOrderPaymentMethod(String orderPaymentMethod) {
		this.orderPaymentMethod = orderPaymentMethod;
	}

	public void setOrderpayamentTxnId(String orderpayamentTxnId) {
		this.orderpayamentTxnId = orderpayamentTxnId;
	}

	public void setOrderPaymentCustContact(String orderPaymentCustContact) {
		this.orderPaymentCustContact = orderPaymentCustContact;
	}

	public List<OrderDetail> getOrderDetail() {
		return orderDetail;
	}

	public void setOrderDetail(List<OrderDetail> orderDetail) {
		this.orderDetail = orderDetail;
	}

}
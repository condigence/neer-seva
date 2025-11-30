package com.condigence.nsorderservice.entity;



import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Table(name = "cust_order")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = {"orderDetail"})
public class Order {

	@EqualsAndHashCode.Include
	@Id
	@Column(name = "id")
	// Use database sequence 'order_seq' for id generation.
	@SequenceGenerator(name = "order_seq_gen", sequenceName = "order_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_seq_gen")
	private Long orderId;

	@Column(name = "order_number")
	private String orderNumber;
	
	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<OrderDetail> orderDetail;

	@Column(name = "order_date")
	private LocalDate orderDate;

	@Column(name = "order_time")
	private LocalTime orderTime;

	@Column(name = "order_from_cust_id")
	private Long orderFromCustId;

	@Column(name = "order_to_vendor_id")
	private Long orderToVendorId;

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

}

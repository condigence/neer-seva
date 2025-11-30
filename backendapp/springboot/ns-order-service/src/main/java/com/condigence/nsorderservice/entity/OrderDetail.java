package com.condigence.nsorderservice.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Table(name = "order_detail")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = {"order"})
public class OrderDetail {

	@EqualsAndHashCode.Include
	@Id
	@Column(name = "order_detail_id")
	@SequenceGenerator(name = "order_detail_seq_gen", sequenceName = "order_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_detail_seq_gen")
	private Long orderDetailId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_id")
    @JsonBackReference
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

}

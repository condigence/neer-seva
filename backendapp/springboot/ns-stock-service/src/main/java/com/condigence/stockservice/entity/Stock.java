package com.condigence.stockservice.entity;



import jakarta.persistence.*;
import java.util.Date;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "stock")
@Data
@NoArgsConstructor
public class Stock {

	@Id
	// explicitly declare column as auto-incrementing BIGINT; this helps Hibernate's DDL and documents intent
	@Column(name = "id", nullable = false, columnDefinition = "BIGINT AUTO_INCREMENT")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long stockId;

	@Column(name = "item_id")

	private long itemId;

	@Column(name = "shop_id")

	private long shopId;

	@Column(name = "quantity")
	private int stockQuantity;

	@Column(name = "date_created")

	private Date stockDateCreated;

	@Column(name = "created_by_user")

	private long stockCreatedByUser;

	@Column(name = "is_available")

	private String stockIsAvailable;

	@Column(name = "is_deleted")

	private String stockIsDeleted;

    @Column(name = "price", nullable = true)
    private Integer price;

    @Column(name = "mrp", nullable = true)
    private Integer mrp;



}

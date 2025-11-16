package com.condigence.stockservice.entity;



import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "stock")
public class Stock {

	public Stock() {
		super();
		// TODO Auto-generated constructor stub
	}

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

//	@Getter
//	@Setter
//	@OneToMany(cascade = CascadeType.ALL)
//	@JoinTable(name = "stock_item", joinColumns = @JoinColumn(name = "stockId"), inverseJoinColumns = @JoinColumn(name = "itemId"))
//	private List<Item> stockItem;

	@Column(name = "date_created")

	private Date stockDateCreated;

	@Column(name = "created_by_user")

	private long stockCreatedByUser;

	@Column(name = "is_available")

	private String stockIsAvailable;

	@Column(name = "is_deleted")

	private String stockIsDeleted;

	public long getItemId() {
		return itemId;
	}

	public long getShopId() {
		return shopId;
	}

	public void setItemId(long itemId) {
		this.itemId = itemId;
	}

	public void setShopId(long shopId) {
		this.shopId = shopId;
	}

	public Long getStockId() {
		return stockId;
	}

	public Date getStockDateCreated() {
		return stockDateCreated;
	}

	public long getStockCreatedByUser() {
		return stockCreatedByUser;
	}

	public String isStockIsAvailable() {
		return stockIsAvailable;
	}

	public String getStockIsDeleted() {
		return stockIsDeleted;
	}

	public void setStockId(Long stockId) {
		this.stockId = stockId;
	}

	public void setStockDateCreated(Date stockDateCreated) {
		this.stockDateCreated = stockDateCreated;
	}

	public void setStockCreatedByUser(long stockCreatedByUser) {
		this.stockCreatedByUser = stockCreatedByUser;
	}

	public void setStockIsAvailable(String stockIsAvailable) {
		this.stockIsAvailable = stockIsAvailable;
	}

	public void setStockIsDeleted(String stockIsDeleted) {
		this.stockIsDeleted = stockIsDeleted;
	}

	public int getStockQuantity() {
		return stockQuantity;
	}

	public void setStockQuantity(int stockQuantity) {
		this.stockQuantity = stockQuantity;
	}

	@Override
	public String toString() {
		return "Stock [stockId=" + stockId + ", itemId=" + itemId + ", shopId=" + shopId + ", stockQuantity="
				+ stockQuantity + ", stockDateCreated=" + stockDateCreated + ", stockCreatedByUser="
				+ stockCreatedByUser + ", stockIsAvailable=" + stockIsAvailable + ", stockIsDeleted=" + stockIsDeleted
				+ "]";
	}

}

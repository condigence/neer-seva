package com.condigence.neerseva.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "stock")
@ToString
public class Stock {

	public Stock() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Id
	@Column(name = "id")
	@Getter
	@Setter
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long stockId;

	@Column(name = "itemId")
	@Getter
	@Setter
	private long itemId;

	@Column(name = "shopId")
	@Getter
	@Setter
	private long shopId;

	@Column(name = "quantity")
	@Getter
	@Setter
	private int stockQuantity;

//	@Getter
//	@Setter
//	@OneToMany(cascade = CascadeType.ALL)
//	@JoinTable(name = "stock_item", joinColumns = @JoinColumn(name = "stockId"), inverseJoinColumns = @JoinColumn(name = "itemId"))
//	private List<Item> stockItem;

	@Column(name = "dateCreated")
	@Getter
	@Setter
	private Date stockDateCreated;

	@Column(name = "createdByUser")
	@Getter
	@Setter
	private long stockCreatedByUser;

	@Column(name = "isAvailable")
	@Getter
	@Setter
	private String stockIsAvailable;

	@Column(name = "isDeleted")
	@Getter
	@Setter
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

	public long getStockId() {
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

	public void setStockId(long stockId) {
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

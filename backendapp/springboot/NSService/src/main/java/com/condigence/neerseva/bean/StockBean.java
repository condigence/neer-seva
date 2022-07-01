package com.condigence.neerseva.bean;

public class StockBean {

	private Long id;

	private int quantity;

	public Long getId() {
		return id;
	}

	public int getQuantity() {
		return quantity;
	}

	@Override
	public String toString() {
		return "StockBean [id=" + id + ", quantity=" + quantity + ", userId=" + userId + ", itemId=" + itemId
				+ ", shopId=" + shopId + "]";
	}

	public Long getUserId() {
		return userId;
	}

	public Long getItemId() {
		return itemId;
	}

	public Long getShopId() {
		return shopId;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}

	private Long userId;

	private Long itemId;
	
	private Long shopId;

	
}

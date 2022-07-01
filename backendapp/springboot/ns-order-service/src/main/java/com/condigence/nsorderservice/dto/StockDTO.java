package com.condigence.nsorderservice.dto;

public class StockDTO {

	private long id;

	private int quantity;

	private UserDTO user;

	private ItemDTO item;
	
	private ShopDTO shop;

	public long getId() {
		return id;
	}

	public int getQuantity() {
		return quantity;
	}

	public UserDTO getUser() {
		return user;
	}

	public ItemDTO getItem() {
		return item;
	}

	public ShopDTO getShop() {
		return shop;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public void setUser(UserDTO user) {
		this.user = user;
	}

	public void setItem(ItemDTO item) {
		this.item = item;
	}

	public void setShop(ShopDTO shop) {
		this.shop = shop;
	}

	@Override
	public String toString() {
		return "StockDTO [id=" + id + ", quantity=" + quantity + ", user=" + user + ", item=" + item + ", shop=" + shop
				+ "]";
	}

	

}

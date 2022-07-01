package com.condigence.nsorderservice.dto;

import java.util.List;

public class OrderDetailDTO {

	
	private VendorDTO vendor;

	public ShopDTO getShop() {
		return shop;
	}

	public void setShop(ShopDTO shop) {
		this.shop = shop;
	}

	private ShopDTO shop;

	
	private CustomerDTO customer;

	
	private List<ItemDTO> items;


	public VendorDTO getVendor() {
		return vendor;
	}


	public List<ItemDTO> getItems() {
		return items;
	}


	public void setVendor(VendorDTO vendor) {
		this.vendor = vendor;
	}


	public void setItems(List<ItemDTO> items) {
		this.items = items;
	}


	public CustomerDTO getCustomer() {
		return customer;
	}


	public void setCustomer(CustomerDTO customer) {
		this.customer = customer;
	}

}

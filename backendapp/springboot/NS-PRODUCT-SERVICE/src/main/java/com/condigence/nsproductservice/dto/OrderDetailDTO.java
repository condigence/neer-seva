package com.condigence.nsproductservice.dto;

import java.util.List;

public class OrderDetailDTO {

	
	private VendorDTO vendor;

	
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

package com.condigence.stockservice.dto;

import java.util.List;

import lombok.Data;

@Data
public class OrderDetailDTO {

	private VendorDTO vendor;

	private ShopDTO shop;
	
	private CustomerDTO customer;

	private List<ItemDTO> items;

}

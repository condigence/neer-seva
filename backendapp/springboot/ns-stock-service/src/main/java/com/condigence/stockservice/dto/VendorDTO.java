package com.condigence.stockservice.dto;

import java.util.List;

import lombok.Data;

@Data
public class VendorDTO {

	private Long vendorId;
	
	private String name;
	
	private String contact;
	
	private String email;

	private Long imageId;
	
	private List<ShopDTO> shopList;

	private byte[] pic;

	private List<AddressDTO> addressList;

}

package com.condigence.stockservice.dto;

import java.util.List;

import lombok.Data;

@Data
public class CustomerDTO {

	private Long customerId;
	
	private String name;
	
	private String email;
	
	private String contact;
	
	private Long imageId;

	private byte[] pic;
	
	private List<AddressDTO> addressList;

}

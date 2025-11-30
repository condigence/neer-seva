package com.condigence.stockservice.dto;

import lombok.Data;

@Data
public class ShopDTO {

	private long id;

	private String name;

	private Long imageId;
	
	private byte[] pic;

	private String type;

	private Long addressId;

	private Long userId;

	private String code;

	private String branch;

}

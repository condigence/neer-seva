package com.condigence.nsorderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShopDTO {

	private Long id;

	private String name;

	private Long imageId;
	
	private byte[] pic;

	private String type;

	private Long addressId;

	private Long userId;

	private String code;

	private String branch;

	private String address;

	private String contact;

}

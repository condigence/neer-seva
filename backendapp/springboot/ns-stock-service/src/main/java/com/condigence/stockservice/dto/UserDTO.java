package com.condigence.stockservice.dto;

import lombok.Data;

@Data
public class UserDTO {

	private Long id;

	private String name;

	private String contact;

	private String email;

	private String otp;

	private String type;

	private String fullName;

	private Long imageId;

	private byte[] pic;

	private String description;

	private Long addressId;
	
	private boolean isActive;

}

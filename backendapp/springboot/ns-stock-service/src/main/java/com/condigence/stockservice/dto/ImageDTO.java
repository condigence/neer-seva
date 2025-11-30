package com.condigence.stockservice.dto;

import lombok.Data;

@Data
public class ImageDTO {

	private Long id;

	private String name;
	
	private Long imageId;

	private byte[] pic;

}

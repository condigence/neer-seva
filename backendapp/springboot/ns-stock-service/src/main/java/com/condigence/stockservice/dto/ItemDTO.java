package com.condigence.stockservice.dto;

import java.util.Date;

import lombok.Data;

@Data
public class ItemDTO {

	private Long id;
	private byte[] pic;
	private String name;
	private Integer price;
	private Integer mrp;
	private Integer dispPrice;
	private String code;
	private Integer discount;
	private String type;
	private String description;
	private Integer capacity;
	private Long brandId;
	private Long imageId;
	private Date dateCreated;
}


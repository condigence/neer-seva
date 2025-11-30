package com.condigence.stockservice.dto;

import lombok.Data;

@Data
public class StockDTO {

	private long id;

	private int quantity;

	private UserDTO user;

	private ItemDTO item;

	private ShopDTO shop;

}

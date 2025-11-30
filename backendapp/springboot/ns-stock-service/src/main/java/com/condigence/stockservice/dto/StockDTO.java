package com.condigence.stockservice.dto;

import lombok.Data;

@Data
public class StockDTO {

	private long id;

	private int quantity;

	private ItemDTO item;

	private long shopId;

}

package com.condigence.nsorderservice.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockDTO {

	private Long stockId;

	@JsonProperty("itemId")
	@JsonAlias({"id", "item_id", "itemId"})
	private Long itemId;

	private Long shopId;
	private Integer quantity;
}

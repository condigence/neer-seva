package com.condigence.stockservice.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

@Data
public class StockItemDTO {

    private Long stockId;

    private int quantity;

    @JsonAlias({"id", "itemId"})
    private Long itemId;

}

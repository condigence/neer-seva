package com.condigence.nsproductservice.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class StockDTO {

    private long id;

    private int quantity;

    private UserDTO user;

    private ItemDTO item;

    private ShopDTO shop;

}

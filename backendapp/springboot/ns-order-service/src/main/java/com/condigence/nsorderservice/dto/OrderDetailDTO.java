package com.condigence.nsorderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailDTO {

	private Long orderDetailId;
	private Long orderItemId;
	private Integer orderItemQuantity;
	private Integer orderItemPrice;
	private Integer orderTotalamount;
	private Integer orderDiscount;
	private Integer orderSubTotal;
	private Integer orderServiceCharge;
	private Integer orderGST;

	private VendorDTO vendor;
	private ShopDTO shop;
	private CustomerDTO customer;
	private List<ItemDTO> items;
    private List<StockDTO> stockItems;

}

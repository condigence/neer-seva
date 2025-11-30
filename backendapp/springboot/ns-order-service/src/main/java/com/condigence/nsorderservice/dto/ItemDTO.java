package com.condigence.nsorderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemDTO {

	private Long id;
	
	private byte[] pic;
	private String name;
	private Integer price;
	private Integer mrp;
	private Integer dispPrice;
	@Override
	public String toString() {
		return "ItemDTO [id=" + id + ", pic=" + Arrays.toString(pic) + ", name=" + name + ", price=" + price + ", mrp="
				+ mrp + ", dispPrice=" + dispPrice + ", quantity=" + quantity + ", code=" + code + ", discount="
				+ discount + ", type=" + type + ", description=" + description + ", capacity=" + capacity + ", brandId="
				+ brandId + ", imageId=" + imageId + ", dateCreated=" + dateCreated + "]";
	}

	private Integer quantity;
	private String code;
	private Integer discount;
	private String type;
	private String description;
	private Integer capacity;
	private Long brandId;
	private Long imageId;
	private Date dateCreated;

}

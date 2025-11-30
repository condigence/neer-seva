package com.condigence.nsorderservice.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VendorDTO {

	private Long vendorId;
	private String name;
	private String contact;
	private String email;
	private Long imageId;
	private byte[] pic;
	private List<ShopDTO> shopList;
	private List<AddressDTO> addressList;

}

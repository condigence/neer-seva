package com.condigence.nsorderservice.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDTO {

	private Long customerId;
	private String name;
	private String email;
	private String contact;
	private Long imageId;
	private byte[] pic;
	List<AddressDTO> addressList;


}

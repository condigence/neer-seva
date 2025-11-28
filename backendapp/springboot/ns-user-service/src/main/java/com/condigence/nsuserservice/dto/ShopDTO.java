package com.condigence.nsuserservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ShopDTO {

	private long id;

	private String name;

	private Long imageId;
	
	private byte[] pic;

	private String type;

	private Long addressId;

	private Long userId;

	private String code;

	private String branch;

}

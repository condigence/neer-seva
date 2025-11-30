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
public class ImageDTO {


	private Long id;

	private String name;
	
	private Long imageId;

	private byte[] pic;

}

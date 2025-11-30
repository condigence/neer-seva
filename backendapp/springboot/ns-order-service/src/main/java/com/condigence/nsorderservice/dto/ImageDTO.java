package com.condigence.nsorderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImageDTO {

	private Long id;
	private String name;
	private String type;
	private byte[] pic;

}

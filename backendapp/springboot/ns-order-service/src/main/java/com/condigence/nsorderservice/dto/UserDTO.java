package com.condigence.nsorderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

	private Long id;
	private String username;
	private String password;
	private String role;
	private String contact;

	// Added fields expected by OrderService
	private String name;
	private String email;
	private Long imageId;

}

package com.condigence.nsproductservice.client;

import java.util.List;

import com.condigence.nsproductservice.dto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;



@FeignClient(name = "ns-user-service")
public interface EmployeeClient {

	@GetMapping("/neerseva/api/v1/users/{id}")
	List<UserDTO> findById(@PathVariable("id") Long id);
	
}

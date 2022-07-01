package com.condigence.nsuserservice.controller;

import com.condigence.nsuserservice.dto.AddressDTO;
import com.condigence.nsuserservice.dto.ImageDTO;
import com.condigence.nsuserservice.dto.UserDTO;
import com.condigence.nsuserservice.entity.Address;
import com.condigence.nsuserservice.entity.User;
import com.condigence.nsuserservice.service.UserService;
import com.condigence.nsuserservice.util.AppProperties;
import com.condigence.nsuserservice.util.CustomErrorType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/neerseva/api")
public class WalletController {

	public static final Logger logger = LoggerFactory.getLogger(WalletController.class);

	@Autowired
	UserService service;

	@Autowired
	public void setApp(AppProperties app) {
		this.app = app;
	}

	private AppProperties app;
	
	@Autowired
	UserService userService;
	
	@Autowired
	RestTemplate restTemplate;
	
	private static final String USER_SERVICE = "userService";
	
    private  static final String DEPARTMENT_SERVICE_URI= "/departments/";
	
	
	@PostMapping("/balance")
	public Boolean addBalance(@RequestBody Long amount) {
		return userService.addBalance(amount);
	}
	

}

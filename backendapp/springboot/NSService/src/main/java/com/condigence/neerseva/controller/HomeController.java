package com.condigence.neerseva.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.condigence.neerseva.dto.DashboardDTO;
import com.condigence.neerseva.services.DashboardService;

@RestController
@CrossOrigin(origins = "*")
public class HomeController {

	@Autowired
	DashboardService service;

	@GetMapping("/")
	public ResponseEntity<?> home() {
		String name = "Welcome to NeerSeva Home!";
		return ResponseEntity.status(HttpStatus.OK).body(name);
	}

	@GetMapping("/neerseva/api")
	public ResponseEntity<?> welcome() {
		String name = "Welcome to NeerSeva API's! Please contact admin or naviagate to other resources";
		return ResponseEntity.status(HttpStatus.OK).body(name);
	}

	@GetMapping("/neerseva/api/v1/dashboard")
	public ResponseEntity<?> dashboard() {
		DashboardDTO dto = service.getAllDashboardItems();
		return ResponseEntity.status(HttpStatus.OK).body(dto);
	}

}

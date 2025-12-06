package com.condigence.imageservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
@Tag(name = "Home", description = "Home and health check endpoints")
public class HomeController {

	@Operation(summary = "Home", description = "Simple welcome endpoint for NeerSeva Image Service")
	@GetMapping("/")
	public ResponseEntity<?> home() {
		String name = "Welcome to NeerSeva Home Image Service!";
		return ResponseEntity.status(HttpStatus.OK).body(name);
	}
}

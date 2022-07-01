package com.condigence.imageservice.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
public class HomeController {



	@GetMapping("/")
	public ResponseEntity<?> home() {
		String name = "Welcome to NeerSeva Home Image Service!";
		return ResponseEntity.status(HttpStatus.OK).body(name);
	}


}

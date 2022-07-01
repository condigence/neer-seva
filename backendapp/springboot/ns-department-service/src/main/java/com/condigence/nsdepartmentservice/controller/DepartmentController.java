package com.condigence.nsdepartmentservice.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.condigence.nsdepartmentservice.entity.Department;
import com.condigence.nsdepartmentservice.exception.CustomErrorType;
import com.condigence.nsdepartmentservice.service.DepartmentService;

@RestController
@RequestMapping("/departments")
public class DepartmentController {

	@Autowired
	private DepartmentService departmentService;

	@PostMapping("/")
	public Department saveDepartment(@RequestBody Department department) {

		return departmentService.saveDepartment(department);
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> findDepartmentById(@PathVariable("id") long departmentId) {
		Optional<Department> department = departmentService.getByDepartmentId(departmentId);
		if (department.isPresent()) {
			return ResponseEntity.status(HttpStatus.OK).body(department);
		} else {
			return new ResponseEntity(new CustomErrorType(" Department not found with id : "+departmentId+" ",HttpStatus.NOT_FOUND.toString()), HttpStatus.NOT_FOUND);
		}

	}

}

package com.condigence.nsdepartmentservice.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.condigence.nsdepartmentservice.entity.Department;
import com.condigence.nsdepartmentservice.repository.DepartmentRepository;

@Service
public class DepartmentService {

	@Autowired
	private DepartmentRepository departmentRepository;

	public Department saveDepartment(Department department) {
		return departmentRepository.save(department);
	}

	public Optional<Department> getByDepartmentId(Long departmentId) {
		return departmentRepository.getByDepartmentId(departmentId);
	}

}

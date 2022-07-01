package com.condigence.neerseva.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.condigence.neerseva.entity.Address;

public interface AddressRepository extends JpaRepository<Address, Long> {

	List<Address> findByUserId(Long id);

	

}

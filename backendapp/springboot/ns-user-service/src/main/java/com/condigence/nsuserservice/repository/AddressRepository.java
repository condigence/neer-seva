package com.condigence.nsuserservice.repository;


import com.condigence.nsuserservice.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Long> {

	List<Address> findByUserId(Long id);

	

}

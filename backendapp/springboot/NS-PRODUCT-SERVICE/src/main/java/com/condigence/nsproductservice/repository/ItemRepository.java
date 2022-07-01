package com.condigence.nsproductservice.repository;


import com.condigence.nsproductservice.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
	
	List<Item> findByBrandId(Long id);



}

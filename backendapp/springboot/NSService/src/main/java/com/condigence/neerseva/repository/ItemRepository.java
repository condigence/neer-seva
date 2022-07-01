package com.condigence.neerseva.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.condigence.neerseva.entity.Item;

public interface ItemRepository extends JpaRepository<Item, Long> {
	
	List<Item> findByBrandId(Long id);

}

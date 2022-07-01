package com.condigence.neerseva.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.condigence.neerseva.entity.Shop;

public interface ShopRepository extends JpaRepository<Shop, Long> {
	
//	Optional<Shop> findByUserId(Long id);
	
	List<Shop> findByUserId(Long id);
}
	
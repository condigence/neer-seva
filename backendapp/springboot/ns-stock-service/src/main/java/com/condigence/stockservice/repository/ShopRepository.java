package com.condigence.stockservice.repository;

import com.condigence.stockservice.entity.Shop;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShopRepository extends JpaRepository<Shop, Long> {
	
//	Optional<Shop> findByUserId(Long id);
	
	List<Shop> findByUserId(Long id);
}
	
package com.condigence.stockservice.repository;


import com.condigence.stockservice.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StockRepository extends JpaRepository<Stock, Long> {
	
	@Query("SELECT s FROM Stock s where s.itemId = :itemId and s.shopId = :shopId")
	Optional<Stock> getItemStockForShop(@Param("itemId") long itemId, @Param("shopId") long shopId);

	@Query("SELECT s FROM Stock s where s.shopId = :shopId")
	List<Stock> getStockForShop(@Param("shopId") long shopId);

}

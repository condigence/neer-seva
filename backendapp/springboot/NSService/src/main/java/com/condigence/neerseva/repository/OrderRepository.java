package com.condigence.neerseva.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.condigence.neerseva.entity.Order;


public interface OrderRepository extends CrudRepository<Order, Long> {


	@Query("SELECT order FROM Order order where order.orderToVendorId = :orderToVendorId ORDER BY order.orderDate DESC")
	List<Order> getByorderToVendorId(@Param("orderToVendorId")Long orderToVendorId);

//	@Query("SELECT order FROM Order order where order.orderByCustId = :orderByCustId ORDER BY order.orderDate DESC")
//	List<Order> getByorderByCustId(@Param("orderByCustId")Long orderByCustId);
//	
	
//	@Query("SELECT order FROM Order order where order.orderByCustId = :orderByCustId")
//	List<OrderDetail> getByordersByCustId(@Param("orderByCustId")Long orderByCustId);
	
	List<Order> findFirst5ByOrderFromCustIdOrderByOrderDateDesc(Long orderFromCustId);
	
	
	List<Order> findFirst5ByOrderToVendorIdOrderByOrderDateDesc(Long orderToVendorId);
	

}

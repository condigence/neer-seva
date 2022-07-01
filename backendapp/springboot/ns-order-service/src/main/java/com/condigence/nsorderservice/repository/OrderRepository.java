package com.condigence.nsorderservice.repository;


import com.condigence.nsorderservice.entity.Order;
import com.condigence.nsorderservice.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface OrderRepository extends JpaRepository<Order, Long> {


	@Query("SELECT order FROM Order order where order.orderToVendorId = :orderToVendorId ORDER BY order.orderDate DESC")
	List<Order> getByorderToVendorId(@Param("orderToVendorId")Long orderToVendorId);

	@Query("SELECT order FROM Order order where order.orderFromCustId = :orderFromCustId ORDER BY order.orderDate DESC")
	List<Order> getByOrderFromCustId(@Param("orderFromCustId")Long orderFromCustId);
//
//
//	@Query("SELECT order FROM Order order where order.orderByCustId = :orderByCustId")
//	List<OrderDetail> getByordersByCustId(@Param("orderByCustId")Long orderByCustId);
	
//	List<Order> findFirst5ByOrderFromCustIdOrderByOrderDateDesc(Long orderFromCustId);
//
//
//	List<Order> findFirst5ByOrderToVendorIdOrderByOrderDateDesc(Long orderToVendorId);
	

}

package com.condigence.neerseva.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.condigence.neerseva.entity.OrderDetail;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {

	Optional<OrderDetail> getByOrderItemId(long itemId);

}

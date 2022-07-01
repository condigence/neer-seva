package com.condigence.nsorderservice.repository;


import com.condigence.nsorderservice.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {

	Optional<OrderDetail> getByOrderItemId(long itemId);

}

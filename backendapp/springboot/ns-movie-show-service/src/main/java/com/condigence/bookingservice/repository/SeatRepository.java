package com.condigence.bookingservice.repository;

import com.condigence.bookingservice.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SeatRepository extends JpaRepository<Seat, Long> {
    List<Seat> findByShowIdAndSeatStatus(Long showId, String seatStatus);
    List<Seat> findByIdIn(List<Long> ids);

}
	
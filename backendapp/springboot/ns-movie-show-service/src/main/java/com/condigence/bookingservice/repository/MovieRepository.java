package com.condigence.bookingservice.repository;

import com.condigence.bookingservice.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository extends JpaRepository<Movie, Long> {
}
	
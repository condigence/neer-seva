package com.condigence.neerseva.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.condigence.neerseva.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByContact(String contact);
	
	Optional<User> findByOtp(String otp);
	
	long countByType(String type);

}

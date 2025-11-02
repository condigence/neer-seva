package com.condigence.nsuserservice.repository;

import java.util.Optional;

import com.condigence.nsuserservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByContact(String contact);

	Optional<User> findByOtp(String otp);

	long countByType(String type);

	// Count active users (is_active = 'Y')
	long countByIsActive(String isActive);

	// Case-insensitive type count convenience
	long countByTypeIgnoreCase(String type);

}
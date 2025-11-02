package com.condigence.imageservice.repository;

import com.condigence.imageservice.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


public interface ImageRepository extends JpaRepository<Image, Long> {

	@Query("SELECT img FROM Image img where img.imageName = :imageName")
	Optional<Image> getByName(@Param("imageName") String imageName);

	@Query("SELECT img FROM Image img where img.id = :id")
	Optional<Image> findByImageId(@Param("id") Long id);

	// Use standard JpaRepository.findAll() for all images
}

package com.condigence.imageservice.repository;

import com.condigence.imageservice.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


public interface ImageRepository extends JpaRepository<Image, Long> {

	@Query("SELECT img FROM Image img where img.imageName = :imageName")
	Optional<Image> getimageId(@Param("imageName") String imageName);

}

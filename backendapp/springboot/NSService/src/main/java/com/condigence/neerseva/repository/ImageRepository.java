package com.condigence.neerseva.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.condigence.neerseva.entity.Image;


public interface ImageRepository extends JpaRepository<Image, Long> {

	@Query("SELECT img FROM Image img where img.imageName = :imageName")
	Optional<Image> getimageId(@Param("imageName") String imageName);

}

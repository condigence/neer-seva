package com.condigence.nsproductservice.repository;


import com.condigence.nsproductservice.model.Brand;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BrandRepository extends JpaRepository<Brand, Long> {
}

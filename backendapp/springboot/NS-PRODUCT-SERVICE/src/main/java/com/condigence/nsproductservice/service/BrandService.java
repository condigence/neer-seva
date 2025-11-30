package com.condigence.nsproductservice.service;


import com.condigence.nsproductservice.bean.BrandBean;
import com.condigence.nsproductservice.model.Brand;
import com.condigence.nsproductservice.repository.BrandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class BrandService {

	@Autowired
	BrandRepository brandRepository;

	public List<Brand> getAll() {
		return brandRepository.findAll();
	}


	public Optional<Brand> getById(Long id) {
		return brandRepository.findById(id);
	}

	public void save(BrandBean brandBean) {
		Brand brand = new Brand();
		brand.setName(brandBean.getName());
		brand.setImageId(brandBean.getImageId());
		brand.setCreatedByUser(brandBean.getCreatedByUserId());
		brand.setDateCreated(new Date().toString());
		brand.setIsDeleted("N");
		brandRepository.save(brand);
	}

	public Brand update(Brand brand) {
		return brandRepository.save(brand);
	}
	
	public void deleteById(long id) {
		brandRepository.deleteById(id);
	}

}

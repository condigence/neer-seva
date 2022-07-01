package com.condigence.neerseva.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.condigence.neerseva.bean.BrandBean;
import com.condigence.neerseva.dto.BrandDTO;
import com.condigence.neerseva.entity.Brand;
import com.condigence.neerseva.entity.Image;
import com.condigence.neerseva.repository.BrandRepository;
import com.condigence.neerseva.repository.ImageRepository;
import com.condigence.neerseva.util.ImageUtil;

@Service
public class BrandService {

	@Autowired
	BrandRepository brandRepository;
	
	@Autowired
	ImageRepository imageRepository;


	public static final Logger logger = LoggerFactory.getLogger(ImageUtil.class);

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

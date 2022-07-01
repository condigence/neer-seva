package com.condigence.neerseva.services;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.condigence.neerseva.dto.ShopDTO;
import com.condigence.neerseva.entity.Shop;
import com.condigence.neerseva.repository.ShopRepository;

@Service
public class ShopService {

	@Autowired
	ShopRepository repository;

	public static final Logger logger = LoggerFactory.getLogger(ShopService.class);

	public List<Shop> getAll() {
		return repository.findAll();
	}

	public Optional<Shop> getById(Long id) {
		return repository.findById(id);
	}

	public Shop save(ShopDTO dto) {
		Shop shop = new Shop();
		shop.setShopName(dto.getName());
		shop.setShopType(dto.getType());
		shop.setUserId(dto.getUserId());
		shop.setShopBranch(dto.getBranch());
		if(dto.getImageId() != 0) {
			shop.setImageId(dto.getImageId());
		}
		
		return repository.save(shop);
	}
	
	public Shop update(Shop shop) {
		return repository.save(shop);
	}
	
	public void deleteById(long id) {
		repository.deleteById(id);
	}

	public List<Shop> getByVendorId(Long id) {
		return repository.findByUserId(id);
	}

}

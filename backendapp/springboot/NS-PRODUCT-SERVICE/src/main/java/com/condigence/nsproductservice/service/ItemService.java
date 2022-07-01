package com.condigence.nsproductservice.service;

import com.condigence.nsproductservice.dto.ItemDTO;
import com.condigence.nsproductservice.model.Item;
import com.condigence.nsproductservice.repository.ItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ItemService {
	public static final Logger logger = LoggerFactory.getLogger(ItemService.class);

	@Autowired
	private ItemRepository itemRepo;

	public Item saveItem(ItemDTO dto) {

		Item item = new Item();

		item.setName(dto.getName());
		item.setImageId(dto.getImageId());
		item.setCapacity(dto.getCapacity());
		item.setBrandId(dto.getBrandId());
		//item.setCode(dto.getName()+dto.getCapacity()+);
		//item.setDiscount(dto.getDiscount());
		//item.setDescription(dto.getDescription());
		item.setDispPrice(dto.getMrp() - dto.getMrp() * 10/100);
		item.setMrp(dto.getMrp());
		item.setPrice(dto.getMrp() + dto.getMrp() * 10/100);
		return itemRepo.save(item);
	}

	public void deleteItemById(long itemId) {
		itemRepo.deleteById(itemId);
	}
//
//	public Item updateItemById(long itemId) {
//		Item item = itemRepo.findById(itemId).get();
//		if (null != item) {
//			itemRepo.save(item);
//		}
//		return item;
//	}
//
//	public Item getItemByOrderItemId(long orderItemId) {
//		// TODO Auto-generated method stub
//		return itemRepo.findById(orderItemId).get();
//	}

	public List<Item> getItems() {
		return itemRepo.findAll();
	}

	public Optional<Item> getItemById(Long id) {
		return itemRepo.findById(id);
	}
	
	public List<Item> getItemByBrandId(Long id) {
		return itemRepo.findByBrandId(id);
	}


	public Item update(Item item) {
		return itemRepo.save(item);
		
	}

}

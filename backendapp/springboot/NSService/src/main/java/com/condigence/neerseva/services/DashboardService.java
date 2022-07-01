package com.condigence.neerseva.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.condigence.neerseva.dto.DashboardDTO;
import com.condigence.neerseva.repository.BrandRepository;
import com.condigence.neerseva.repository.ItemRepository;
import com.condigence.neerseva.repository.OrderRepository;
import com.condigence.neerseva.repository.ShopRepository;
import com.condigence.neerseva.repository.UserRepository;

@Service
public class DashboardService {

	public static final Logger logger = LoggerFactory.getLogger(DashboardService.class);

	@Autowired
	BrandRepository brandRepository;

	@Autowired
	ItemRepository itemRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	ShopRepository shopRepository;

	@Autowired
	OrderRepository orderRepository;

	public DashboardDTO getAllDashboardItems() {

		DashboardDTO dto = new DashboardDTO();
		Long totalUsers = userRepository.count();
		Long customerCount = userRepository.countByType("CUSTOMER");
		Long vendorCount = userRepository.countByType("VENDOR");
		dto.setTotalCustomers(customerCount);
		dto.setTotalVendors(vendorCount);
		dto.setTotalUsers(totalUsers);
		Long brandCount = brandRepository.count();
		dto.setTotalBrands(brandCount);
		Long itemCount = itemRepository.count();
		dto.setTotalItems(itemCount);
		Long totalOrders = orderRepository.count();
		dto.setTotalOrders(totalOrders);
		Long totalShops = shopRepository.count();
		dto.setTotalShops(totalShops);
		return dto;
	}
}

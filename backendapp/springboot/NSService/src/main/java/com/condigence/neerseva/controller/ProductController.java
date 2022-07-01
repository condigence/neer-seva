package com.condigence.neerseva.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.condigence.neerseva.bean.BrandBean;
import com.condigence.neerseva.dto.BrandDTO;
import com.condigence.neerseva.dto.ItemDTO;
import com.condigence.neerseva.dto.OrderDTO;
import com.condigence.neerseva.dto.OrderDetailDTO;
import com.condigence.neerseva.entity.Brand;
import com.condigence.neerseva.entity.Image;
import com.condigence.neerseva.entity.Item;
import com.condigence.neerseva.entity.Order;
import com.condigence.neerseva.entity.Stock;
import com.condigence.neerseva.services.BrandService;
import com.condigence.neerseva.services.ImageService;
import com.condigence.neerseva.services.ItemService;
import com.condigence.neerseva.services.OrderService;
import com.condigence.neerseva.util.AppProperties;
import com.condigence.neerseva.util.CustomErrorType;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/neerseva/api")
public class ProductController {

	public static final Logger logger = LoggerFactory.getLogger(ProductController.class);

	@Autowired
	BrandService brandService;

	@Autowired
	ItemService itemService;

	@Autowired
	ImageService imageService;

	@Autowired
	private OrderService orderService;

	@Autowired
	public void setApp(AppProperties app) {
		this.app = app;
	}

	private AppProperties app;

/////////////////////////  Brands Start//////////////////////

	@PostMapping(value = "/v1/brands")
	public ResponseEntity<?> addBrands(@RequestBody BrandBean brandBean) {
		logger.info("Entering addBrands with Brand Details >>>>>>>>  : {}", brandBean);
		HttpHeaders headers = new HttpHeaders();
		brandService.save(brandBean);
		return new ResponseEntity<>(headers, HttpStatus.CREATED);
	}

	@GetMapping("/v1/brands")
	public ResponseEntity<?> getAllBrands() {

		List<BrandDTO> dtos = new ArrayList<>();

		List<Brand> brands = brandService.getAll();
		for (Brand brand : brands) {
			BrandDTO dto = new BrandDTO();
			dto.setId(brand.getId());
			dto.setImageId(brand.getImageId());
			dto.setName(brand.getName());
			if (brand.getImageId() != null) {
				dto.setPic(getPicById(brand.getImageId()).getPic());
			}
			dtos.add(dto);

		}
		return ResponseEntity.status(HttpStatus.OK).body(dtos);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@DeleteMapping(value = "/v1/brands/{id}")
	public ResponseEntity<?> deleteBrand(@PathVariable("id") long id) {
		logger.info("Fetching & Deleting Brand with id {}", id);
		Optional<Brand> brand = brandService.getById(id);
		if (brand.isPresent()) {
			brandService.deleteById(id);
		} else {
			logger.error("Unable to delete. Brand with id {} not found.", id);
			return new ResponseEntity(new CustomErrorType("Unable to delete. Brand with id " + id + " not found."),
					HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Stock>(HttpStatus.OK);
	}

	@GetMapping("/v1/brands/{id}")
	public ResponseEntity<?> getBrand(@PathVariable("id") Long id) {

		System.out.println("inside brandimage!");
		BrandDTO dto = new BrandDTO();
		Optional<Brand> brand = brandService.getById(id);
		if (brand.isPresent()) {
			System.out.println("Brand is present");
			dto.setId(brand.get().getId());
			dto.setImageId(brand.get().getImageId());
			dto.setName(brand.get().getName());
			if (brand.get().getImageId() != null) {
				dto.setPic(getPicById(brand.get().getImageId()).getPic());

			}
		} else {
			return new ResponseEntity(new CustomErrorType("Brand not found."), HttpStatus.NOT_FOUND);
		}

		return ResponseEntity.status(HttpStatus.OK).body(dto);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@PutMapping(value = "/v1/brands")
	public ResponseEntity<?> updateUser(@RequestBody BrandDTO dto) {
		logger.info("Updating Brand  with id {}", dto.getId());
		System.out.println(dto);
		Optional<Brand> brand = brandService.getById(dto.getId());
		System.out.println(brand.get());
		if (!brand.isPresent()) {
			logger.error("Unable to update. Brand with id {} not found.", dto.getId());
			return new ResponseEntity(
					new CustomErrorType("Unable to upate. Brand with id " + dto.getId() + " not found."),
					HttpStatus.NOT_FOUND);
		} else {
			brand.get().setName(dto.getName());
			brand.get().setImageId(dto.getImageId());
			brandService.update(brand.get());
			return new ResponseEntity<Brand>(brand.get(), HttpStatus.OK);
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@PostMapping(value = "/v1/items")
	public ResponseEntity<?> addItems(@RequestBody ItemDTO itemDTO) {
		logger.info("Entering addBrands with Brand Details >>>>>>>>  : {}", itemDTO);
		HttpHeaders headers = new HttpHeaders();
		Item item = itemService.saveItem(itemDTO);
		if (item == null) {
			return new ResponseEntity(new CustomErrorType("Issue while saving Item"), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<String>(headers, HttpStatus.CREATED);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@PutMapping(value = "/v1/items")
	public ResponseEntity<?> updateItems(@RequestBody ItemDTO dto) {
		logger.info("Updating Item  with id {}", dto.getId());
		System.out.println(dto);
		Optional<Item> itme = itemService.getItemById(dto.getId());
		System.out.println(itme.get());
		if (!itme.isPresent()) {
			logger.error("Unable to update. Item with id {} not found.", dto.getId());
			return new ResponseEntity(
					new CustomErrorType("Unable to upate. Item with id " + dto.getId() + " not found."),
					HttpStatus.NOT_FOUND);
		} else {

			if(!dto.getName().equalsIgnoreCase("")) {
				itme.get().setName(dto.getName());
			}
			
			if(dto.getImageId() != null) {
				itme.get().setImageId(dto.getImageId());
			}
			if(dto.getBrandId() != null) {
				itme.get().setBrandId(dto.getBrandId());
			}
			
			if(dto.getCapacity() != null) {
				itme.get().setCapacity(dto.getCapacity());
			}
			
			if(dto.getMrp() != null) {
				itme.get().setMrp(dto.getMrp());
			}
			
			itemService.update(itme.get());
			return new ResponseEntity<Item>(itme.get(), HttpStatus.OK);
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@DeleteMapping(value = "/v1/items/{id}")
	public ResponseEntity<?> deleteItem(@PathVariable("id") long id) {
		logger.info("Fetching & Deleting Item with id {}", id);
		Optional<Item> item = itemService.getItemById(id);
		if (item.isPresent()) {
			itemService.deleteItemById(id);
		} else {
			logger.error("Unable to delete. Item with id {} not found.", id);
			return new ResponseEntity(new CustomErrorType("Unable to delete. Item with id " + id + " not found."),
					HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Stock>(HttpStatus.OK);
	}

	@GetMapping("/v1/items")
	public ResponseEntity<?> getAllItems() {
		List<ItemDTO> dtos = new ArrayList<>();
		List<Item> items = null;
		items = itemService.getItems();
		for (Item item : items) {
			ItemDTO dto = new ItemDTO();
			dto.setId(item.getId());

			dto.setName(item.getName());
			dto.setCode(item.getCode());
			dto.setMrp(item.getMrp());
			dto.setPrice(item.getPrice());
			dto.setBrandId(item.getBrandId());
			dto.setDispPrice(item.getDispPrice());
			if (item.getImageId() != null) {
				dto.setImageId(item.getImageId());
				dto.setPic(getPicById(item.getImageId()).getPic());
			}

			dtos.add(dto);
		}
		return ResponseEntity.status(HttpStatus.OK).body(dtos);
	}

	@GetMapping("/v1/items/{id}")
	public ResponseEntity<?> getItemWithImage(@PathVariable("id") Long id) {

		System.out.println("inside itemImage!");
		ItemDTO dto = new ItemDTO();
		Optional<Item> item = itemService.getItemById(id);
		if (item.isPresent()) {
			dto.setId(item.get().getId());
			dto.setImageId(item.get().getImageId());
			dto.setName(item.get().getName());
			dto.setCapacity(item.get().getCapacity());
			//dto.setDispPrice(item.get().getDispPrice());
			dto.setMrp(item.get().getMrp());
			dto.setQuantity(item.get().getQuantity());
			//dto.setDescription(item.get().getDescription());
			//dto.setDiscount(item.get().getDiscount());
			//dto.setType(item.get().getType());
			dto.setBrandId(item.get().getBrandId());
			//dto.setCode(item.get().getCode());
			//dto.setPrice(item.get().getPrice());
			if (item.get().getImageId() != null) {
				dto.setImageId(item.get().getImageId());
				dto.setPic(getPicById(item.get().getImageId()).getPic());
			}
			
			
			
			// dto.setPic(getPicById(item.get().getImageId()).getPic());
		}

		else {
			return new ResponseEntity(new CustomErrorType("Item not found."), HttpStatus.NOT_FOUND);
		}

		return ResponseEntity.status(HttpStatus.OK).body(dto);
	}

	@GetMapping("/v1/items/vendors/{id}")
	public ResponseEntity<?> getItemsByvendorid(@PathVariable("id") Long id) {

		System.out.println("inside itemImage!");
		ItemDTO dto = new ItemDTO();
		Optional<Item> item = itemService.getItemById(id);
		if (item.isPresent()) {
			dto.setId(item.get().getId());
			dto.setImageId(item.get().getImageId());
			dto.setName(item.get().getName());
			dto.setCapacity(item.get().getCapacity());
			dto.setDispPrice(item.get().getDispPrice());
			dto.setMrp(item.get().getMrp());
			dto.setQuantity(item.get().getQuantity());
			dto.setDescription(item.get().getDescription());
			dto.setDiscount(item.get().getDiscount());
			dto.setType(item.get().getType());
			dto.setBrandId(item.get().getBrandId());
			dto.setCode(item.get().getCode());
			dto.setPrice(item.get().getPrice());
			// dto.setPic(getPicById(item.get().getImageId()).getPic());
		}

		else {
			return new ResponseEntity(new CustomErrorType("Item not found."), HttpStatus.NOT_FOUND);
		}

		return ResponseEntity.status(HttpStatus.OK).body(dto);
	}

	@GetMapping("/v1/items/by/brands/{id}")
	public ResponseEntity<?> getItemsByBrandId(@PathVariable("id") Long id) {

		System.out.println("inside itemImage with brand Id {} is!" + id);
		List<ItemDTO> dtos = new ArrayList<>();
		List<Item> items = itemService.getItemByBrandId(id);
		System.out.println(items);
		for (Item item : items) {
			ItemDTO dto = new ItemDTO();
			dto.setId(item.getId());
			dto.setImageId(item.getImageId());
			dto.setName(item.getName());
			dto.setCapacity(item.getCapacity());
			dto.setDispPrice(item.getDispPrice());
			dto.setMrp(item.getMrp());
			dto.setQuantity(item.getQuantity());
			dto.setDescription(item.getDescription());
			dto.setDiscount(item.getDiscount());
			dto.setType(item.getType());
			dto.setBrandId(item.getBrandId());
			dto.setCode(item.getCode());
			dto.setPrice(item.getPrice());
			// dto.setPic(getPicById(item.get().getImageId()).getPic());
			dtos.add(dto);
		}
		return ResponseEntity.status(HttpStatus.OK).body(dtos);
	}

	////////////////////////////////////// ORDERS////////////////////////////////////////

	@GetMapping("/v1/orders/customer/{id}")
	public ResponseEntity<?> getOrderByCustomerId(@PathVariable("id") Integer custId) {
		logger.info("getOrderByCustomerId with id {}", custId);
		List<OrderDTO> orderList = orderService.getOrderBycustomerId(custId);
		return new ResponseEntity<List<OrderDTO>>(orderList, HttpStatus.OK);

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@PostMapping(value = "/v1/orders/place")
	public ResponseEntity<?> placeOrderItems(@RequestBody OrderDetailDTO orderdetailDTO) {
		logger.info("placeOrderItems detail is>>>>>>>>  : {}", orderdetailDTO);
		HttpHeaders headers = new HttpHeaders();
		boolean orderDetail = false;
		orderDetail = orderService.saveOrderDetail(orderdetailDTO);
		if (orderDetail == false) {
			return new ResponseEntity(new CustomErrorType("Issue while saving order"),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<String>(headers, HttpStatus.CREATED);
	}

	@GetMapping("/v1/orders")
	public ResponseEntity<?> getAll() {
		List<Order> orders = new ArrayList<>();
		orders = orderService.getAllOrders();
		return ResponseEntity.status(HttpStatus.OK).body(orders);
	}

	@GetMapping("/v1/orders/to/vendor/{id}")
	public ResponseEntity<?> getOrderByVendorId(@PathVariable("id") Integer vendorId) {
		logger.info("getOrderByVendorId with id {}", vendorId);
		List<OrderDTO> orderList = orderService.getOrderByVendorId(vendorId);
		return new ResponseEntity<List<OrderDTO>>(orderList, HttpStatus.OK);

	}

	@PatchMapping("/v1/orders/status/update/{id}")
	public ResponseEntity<?> updateOrderStatus(@RequestBody OrderDTO dto, @PathVariable("id") Integer id) {
		OrderDTO newOrder = orderService.save(dto, id);
		return new ResponseEntity<>(newOrder, HttpStatus.OK);
	}

	////////////////////////////////////////////////////////////////////////////////////////////
	private Image getPicById(long imageId) {
		String imagePath = app.getLocation();
		return imageService.getImage(imageId, imagePath);
	}

	///////////////////////////////////////////////////////////////////

}

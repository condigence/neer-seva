package com.condigence.nsproductservice.controller;


import java.util.*;
import java.util.List;

import com.condigence.nsproductservice.bean.BrandBean;

import com.condigence.nsproductservice.dto.*;
import com.condigence.nsproductservice.model.Brand;
import com.condigence.nsproductservice.model.Item;
import com.condigence.nsproductservice.service.BrandService;
import com.condigence.nsproductservice.service.ItemService;
import com.condigence.nsproductservice.util.AppProperties;
import com.condigence.nsproductservice.util.CustomErrorType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/neerseva/api/v1/products")
public class ProductController {

	public static final Logger logger = LoggerFactory.getLogger(ProductController.class);

//	@Autowired
//	private UsersClient proxy;

	@Autowired
	BrandService brandService;

	@Autowired
	ItemService itemService;

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	public void setApp(AppProperties app) {
		this.app = app;
	}

	private AppProperties app;

//	private static final String USER_SERVICE = "userService";
//
//	private  static final String DEPARTMENT_SERVICE_URI= "/departments/";

	private static final String PRODUCT_SERVICE = "productService";

	private  static final String DEPARTMENT_SERVICE_URI= "/departments/";

/////////////////////////  Brands Start//////////////////////

	public ResponseEntity<?> userFallback(Exception exception) {

		return ResponseEntity.status(HttpStatus.OK).body("USER SERVICE IS DOWN! Please contact Admin!");
	}

	/**
	 * Safely fetch image pic from image-service. If any exception occurs or the
	 * response/body is null, this returns null. This prevents the controller
	 * from throwing when image-service returns 401/404 or is down.
	 */
	private byte[] getImagePicSafe(Long imageId) {
		if (imageId == null) return null;
		try {
			ImageDTO imageDTO = restTemplate.getForObject("http://NS-IMAGE-SERVICE/neerseva/api/v1/images/" + imageId+"/data", ImageDTO.class);
			if (imageDTO != null) {
				return imageDTO.getPic();
			}
		} catch (RestClientException ex) {
			// Log and continue without failing the whole request.
			logger.warn("Unable to fetch image from image-service for id {}: {}", imageId, ex.getMessage());
		} catch (Exception ex) {
			logger.error("Unexpected error while fetching image {}: {}", imageId, ex.getMessage());
		}
		return null;
	}

	@PostMapping(value = "/brands")
	public ResponseEntity<?> addBrands(@RequestBody BrandBean brandBean) {
		logger.info("Entering addBrands with Brand Details >>>>>>>>  : {}", brandBean);
		HttpHeaders headers = new HttpHeaders();
		brandService.save(brandBean);
		return new ResponseEntity<>(headers, HttpStatus.CREATED);
	}

	@GetMapping("/brands/")
//	@CircuitBreaker(name=USER_SERVICE,fallbackMethod = "userFallback")
	public ResponseEntity<?> getAllBrands() {

		List<BrandDTO> dtos = new ArrayList<>();
		Map<String, String> uriParams = new HashMap<>();
		uriParams.put("id", String.valueOf(4));

		List<Brand> brands = brandService.getAll();
		for (Brand brand : brands) {
			BrandDTO dto = new BrandDTO();
			dto.setId(brand.getId());
			dto.setImageId(brand.getImageId());
			dto.setName(brand.getName());
			dto.setCreatedByUser(brand.getCreatedByUser());
			//UserDTO userDTO = restTemplate.getForObject("http://NS-USER-SERVICE/neerseva/api/v1/users/"+brand.getCreatedByUser(), UserDTO.class); // Working
			//System.out.println(brand);
			if (brand.getImageId() != null) {
				// Use safe helper to avoid failing the whole request if image-service returns 401/other errors
				byte[] pic = getImagePicSafe(brand.getImageId());
				if (pic != null) {
					dto.setPic(pic);
				}
			}
			dtos.add(dto);
		}
		return ResponseEntity.status(HttpStatus.OK).body(dtos);
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	@CrossOrigin
	@DeleteMapping(value = "/brands/{id}")
	public ResponseEntity<?> deleteBrand(@PathVariable("id") long id) {
		logger.info("Fetching & Deleting Brand with id {}", id);
		System.out.println("Inside delete mapping! ");
		Optional<Brand> brand = brandService.getById(id);
		if (brand.isPresent()) {
			brandService.deleteById(id);
		} else {
			logger.error("Unable to delete. Brand with id {} not found.", id);
			return new ResponseEntity(new CustomErrorType("Unable to delete. Brand with id " + id + " not found."),
					HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Brand>(HttpStatus.OK);
	}

	@GetMapping("/brands/{id}")
	public ResponseEntity<?> getBrand(@PathVariable("id") Long id) {

		System.out.println("inside brandimage!");
		BrandDTO dto = new BrandDTO();
		Optional<Brand> brand = brandService.getById(id);
		if (brand.isPresent()) {
			//System.out.println("Brand is present");
			dto.setId(brand.get().getId());
			dto.setImageId(brand.get().getImageId());
			dto.setName(brand.get().getName());
			dto.setCreatedByUser(brand.get().getCreatedByUser());
			if (brand.get().getImageId() != null) {
				byte[] pic = getImagePicSafe(brand.get().getImageId());
				if (pic != null) {
					dto.setPic(pic);
				}

			}
		} else {
			return new ResponseEntity(new CustomErrorType("Brand not found."), HttpStatus.NOT_FOUND);
		}

		return ResponseEntity.status(HttpStatus.OK).body(dto);
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	@PutMapping(value = "/brands")
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

	@SuppressWarnings({"unchecked", "rawtypes"})
	@PostMapping(value = "/items")
	public ResponseEntity<?> addItems(@RequestBody ItemDTO itemDTO) {
		logger.info("Entering addBrands with Brand Details >>>>>>>>  : {}", itemDTO);
		HttpHeaders headers = new HttpHeaders();
		Item item = itemService.saveItem(itemDTO);
		if (item == null) {
			return new ResponseEntity(new CustomErrorType("Issue while saving Item"), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<String>(headers, HttpStatus.CREATED);
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	@PutMapping(value = "/items")
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

			if (!dto.getName().equalsIgnoreCase("")) {
				itme.get().setName(dto.getName());
			}

			if (dto.getImageId() != null) {
				itme.get().setImageId(dto.getImageId());
			}
			if (dto.getBrandId() != null) {
				itme.get().setBrandId(dto.getBrandId());
			}

			if (dto.getCapacity() != null) {
				itme.get().setCapacity(dto.getCapacity());
			}

			if (dto.getMrp() != null) {
				itme.get().setMrp(dto.getMrp());
			}

			itemService.update(itme.get());
			return new ResponseEntity<Item>(itme.get(), HttpStatus.OK);
		}
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	@DeleteMapping(value = "/items/{id}")
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
		return new ResponseEntity<Item>(HttpStatus.OK);
	}

	@GetMapping("/items")
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
				byte[] pic = getImagePicSafe(item.getImageId());
				if (pic != null) {
					dto.setPic(pic);
				}
			}

			dtos.add(dto);
		}
		return ResponseEntity.status(HttpStatus.OK).body(dtos);
	}

	@GetMapping("/items/{id}")
	public ResponseEntity<?> getItemWithImage(@PathVariable("id") Long id) {

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
			if (item.get().getImageId() != null) {
				dto.setImageId(item.get().getImageId());

				byte[] pic = getImagePicSafe(item.get().getImageId());
				if (pic != null) {
					dto.setPic(pic);
				}
			}


			// dto.setPic(getPicById(item.get().getImageId()).getPic());
		} else {
			return new ResponseEntity(new CustomErrorType("Item not found."), HttpStatus.NOT_FOUND);
		}

		return ResponseEntity.status(HttpStatus.OK).body(dto);
	}

	@GetMapping("/items/by/vendors/{id}")
	public ResponseEntity<?> getItemsByvendorid(@PathVariable("id") Long id) {
		List<ItemDTO> items = restTemplate.getForObject("http://NS-STOCK-SERVICE/neerseva/api/v1/stocks/items/by/vendor/"+id, List.class); // Working
		return ResponseEntity.status(HttpStatus.OK).body(items);
	}

	@GetMapping("/items/by/brands/{id}")
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


}
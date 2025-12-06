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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import com.condigence.nsproductservice.exception.ResourceNotFoundException;
import com.condigence.nsproductservice.exception.TechnicalException;
import com.condigence.nsproductservice.error.ErrorCode;
import com.condigence.nsproductservice.error.ApiErrorResponse;

@Tag(name = "Products", description = "Operations related to product brands and items")
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

		// Try configured absolute fallback URL first (avoid discovery)
		String fallbackBase = null;
		if (this.app != null) {
			// try a few possible property keys
			String[] keys = {"app.image-service.url", "app.image.service.url", "app.image_service.url", "app.imageService.url"};
			for (String k : keys) {
				String v = this.app.getProperty(k);
				if (v != null && !v.isBlank()) { fallbackBase = v; break; }
			}
		}
		if (fallbackBase != null && !fallbackBase.isBlank()) {
			String fbPath = fallbackBase;
			if (!fbPath.endsWith("/")) fbPath += "/";
			fbPath += "neerseva/api/v1/images/" + imageId + "/data";
			try {
				org.springframework.web.client.RestTemplate direct = new org.springframework.web.client.RestTemplate();
				ImageDTO imageDTO = direct.getForObject(fbPath, ImageDTO.class);
				if (imageDTO != null) return imageDTO.getPic();
			} catch (Exception ex) {
				logger.warn("Unable to fetch image from fallback path {}: {}", fbPath, ex.getMessage());
			}
		} else {
			logger.debug("No fallback image-service URL configured (app.image-service.url or alternatives)");
		}

		// Decide whether to try discovery; check both kebab and camel property names
		boolean tryDiscovery = true;
		if (this.app != null) {
			String fetch1 = this.app.getProperty("eureka.client.fetchRegistry");
			String fetch2 = this.app.getProperty("eureka.client.fetch-registry");
			if ((fetch1 != null && fetch1.equalsIgnoreCase("false")) || (fetch2 != null && fetch2.equalsIgnoreCase("false"))) {
				tryDiscovery = false;
			}
		}
		if (!tryDiscovery) {
			logger.debug("Skipping service discovery for NS-IMAGE-SERVICE because eureka.client.fetchRegistry is false");
			return null;
		}

		String servicePath = "http://NS-IMAGE-SERVICE/neerseva/api/v1/images/" + imageId + "/data";
		try {
			ImageDTO imageDTO = restTemplate.getForObject(servicePath, ImageDTO.class);
			if (imageDTO != null) {
				return imageDTO.getPic();
			}
		} catch (IllegalStateException ise) {
			logger.warn("Service discovery failed for NS-IMAGE-SERVICE (no instances): {}", ise.getMessage());
		} catch (Exception ex) {
			logger.warn("Unable to fetch image from discovery path {}: {}", servicePath, ex.getMessage());
		}

		return null;
	}

	@Operation(
            summary = "Create a new brand",
            description = "Creates a new brand with the provided details. Returns 201 on success."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Brand created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid brand payload",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Unexpected server error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class)))
    })
	@RequestMapping(path = {"/brands", "/brands/"}, method = RequestMethod.POST)
	public ResponseEntity<?> addBrands(@RequestBody BrandBean brandBean) {
		logger.info("Entering addBrands with Brand Details >>>>>>>>  : {}", brandBean);
		HttpHeaders headers = new HttpHeaders();
		brandService.save(brandBean);
		return new ResponseEntity<>(headers, HttpStatus.CREATED);
	}

	@Operation(
            summary = "Get all brands",
            description = "Returns a list of all available brands with optional image data embedded."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of brands returned successfully"),
            @ApiResponse(responseCode = "500", description = "Unexpected server error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class)))
    })
	@GetMapping(path = {"/brands", "/brands/"})
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
	@Operation(
            summary = "Delete a brand",
            description = "Deletes a brand identified by its ID. Returns 200 if deletion is successful, 404 if the brand is not found."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Brand deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Brand not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Unexpected server error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class)))
    })
	public ResponseEntity<?> deleteBrand(@PathVariable("id") long id) {
		logger.info("Fetching & Deleting Brand with id {}", id);
		Optional<Brand> brand = brandService.getById(id);
		if (brand.isPresent()) {
			brandService.deleteById(id);
			return new ResponseEntity<Brand>(HttpStatus.OK);
		} else {
			logger.error("Unable to delete. Brand with id {} not found.", id);
			throw new ResourceNotFoundException("Brand not found with id " + id, ErrorCode.BRAND_NOT_FOUND);
		}
	}

	@Operation(
            summary = "Get brand by ID",
            description = "Retrieves a single brand by its unique identifier. Includes image data when available."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Brand details returned successfully"),
            @ApiResponse(responseCode = "404", description = "Brand not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Unexpected server error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class)))
    })
	@GetMapping("/brands/{id}")
	public ResponseEntity<?> getBrand(
			@Parameter(description = "ID of the brand to retrieve", example = "1")
			@PathVariable("id") Long id) {

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
			throw new ResourceNotFoundException("Brand not found with id " + id, ErrorCode.BRAND_NOT_FOUND);
		}

		return ResponseEntity.status(HttpStatus.OK).body(dto);
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	@PutMapping(value = "/brands")
	@Operation(
            summary = "Update a brand",
            description = "Updates an existing brand. Returns 404 if the brand does not exist."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Brand updated successfully"),
            @ApiResponse(responseCode = "404", description = "Brand not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Unexpected server error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class)))
    })
	public ResponseEntity<?> updateUser(@RequestBody BrandDTO dto) {
		logger.info("Updating Brand  with id {}", dto.getId());
		Optional<Brand> brand = brandService.getById(dto.getId());
		if (!brand.isPresent()) {
			logger.error("Unable to update. Brand with id {} not found.", dto.getId());
			throw new ResourceNotFoundException("Brand not found with id " + dto.getId(), ErrorCode.BRAND_NOT_FOUND);
		} else {
			brand.get().setName(dto.getName());
			brand.get().setImageId(dto.getImageId());
			brandService.update(brand.get());
			return new ResponseEntity<Brand>(brand.get(), HttpStatus.OK);
		}
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	@PostMapping(path = {"/items", "/items/"})
	@Operation(
            summary = "Create a new item",
            description = "Creates a new item. Returns 500 if an unexpected error occurs when saving."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Item created successfully"),
            @ApiResponse(responseCode = "500", description = "Unexpected server error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class)))
    })
	public ResponseEntity<?> addItems(@RequestBody ItemDTO itemDTO) {
		logger.info("Entering addItems with Item Details >>>>>>>>  : {}", itemDTO);
		HttpHeaders headers = new HttpHeaders();
		Item item = itemService.saveItem(itemDTO);
		if (item == null) {
			throw new TechnicalException("Issue while saving Item", ErrorCode.INTERNAL_ERROR);
		}
		return new ResponseEntity<>(headers, HttpStatus.CREATED);
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	@PutMapping(value = "/items")
	@Operation(
            summary = "Update an item",
            description = "Updates an existing item. Returns 404 if the item does not exist."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Item updated successfully"),
            @ApiResponse(responseCode = "404", description = "Item not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Unexpected server error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    public ResponseEntity<?> updateItems(@RequestBody ItemDTO dto) {
		logger.info("Updating Item  with id {}", dto.getId());
		Optional<Item> itme = itemService.getItemById(dto.getId());
		if (!itme.isPresent()) {
			logger.error("Unable to update. Item with id {} not found.", dto.getId());
			throw new ResourceNotFoundException("Item not found with id " + dto.getId(), ErrorCode.ITEM_NOT_FOUND);
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
	@Operation(
            summary = "Delete an item",
            description = "Deletes an item by ID. Returns 404 if the item does not exist."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Item deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Item not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Unexpected server error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    public ResponseEntity<?> deleteItem(@PathVariable("id") long id) {
		logger.info("Fetching & Deleting Item with id {}", id);
		Optional<Item> item = itemService.getItemById(id);
		if (item.isPresent()) {
			itemService.deleteItemById(id);
			return new ResponseEntity<Item>(HttpStatus.OK);
		} else {
			logger.error("Unable to delete. Item with id {} not found.", id);
			throw new ResourceNotFoundException("Item not found with id " + id, ErrorCode.ITEM_NOT_FOUND);
		}
	}

	@GetMapping(path = {"/items", "/items/"})
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
	@Operation(
            summary = "Get item by ID",
            description = "Retrieves an item by ID including its image bytes when available."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Item details returned successfully"),
            @ApiResponse(responseCode = "404", description = "Item not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Unexpected server error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class)))
    })
	public ResponseEntity<?> getItemWithImage(@PathVariable("id") Long id) {

		ItemDTO dto = new ItemDTO();
		Optional<Item> item = itemService.getItemById(id);
		if (item.isPresent()) {
			dto.setId(item.get().getId());
			dto.setImageId(item.get().getImageId());
			dto.setName(item.get().getName());
			dto.setCapacity(item.get().getCapacity());
			dto.setDispPrice(item.get().getDispPrice());
			dto.setMrp(item.get().getMrp());
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
			throw new ResourceNotFoundException("Item not found with id " + id, ErrorCode.ITEM_NOT_FOUND);
		}

		return ResponseEntity.status(HttpStatus.OK).body(dto);
	}

	@GetMapping("/items/by/vendors/{id}")
	public ResponseEntity<?> getItemsByvendorid(@PathVariable("id") Long id) {
		try {
			List<ItemDTO> items = restTemplate.getForObject("http://NS-STOCK-SERVICE/neerseva/api/v1/stocks/items/by/vendor/" + id, List.class); // Working
			return ResponseEntity.status(HttpStatus.OK).body(items);
		} catch (IllegalStateException ise) {
			logger.warn("Service discovery failed for NS-STOCK-SERVICE when fetching items for vendor {}: {}", id, ise.getMessage());
			return ResponseEntity.status(HttpStatus.OK).body(Collections.emptyList());
		} catch (Exception ex) {
			logger.warn("Unable to fetch items by vendor {} from stock service: {}", id, ex.getMessage());
			return ResponseEntity.status(HttpStatus.OK).body(Collections.emptyList());
		}
	}

	@GetMapping("/items/by/brands/{id}")
	public ResponseEntity<?> getItemsByBrandId(@PathVariable("id") Long id) {

		List<ItemDTO> dtos = new ArrayList<>();
		List<Item> items = itemService.getItemByBrandId(id);
		for (Item item : items) {
			ItemDTO dto = new ItemDTO();
			dto.setId(item.getId());
			dto.setImageId(item.getImageId());
			dto.setName(item.getName());
			dto.setCapacity(item.getCapacity());
			dto.setDispPrice(item.getDispPrice());
			dto.setMrp(item.getMrp());
			dto.setDescription(item.getDescription());
			dto.setDiscount(item.getDiscount());
			dto.setType(item.getType());
			dto.setBrandId(item.getBrandId());
			dto.setCode(item.getCode());
			dto.setPrice(item.getPrice());
			dtos.add(dto);
		}
		return ResponseEntity.status(HttpStatus.OK).body(dtos);
	}


}

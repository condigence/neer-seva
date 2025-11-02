package com.condigence.nsuserservice.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import com.condigence.nsuserservice.dto.AddressDTO;
import com.condigence.nsuserservice.dto.ImageDTO;
import com.condigence.nsuserservice.dto.ShopDTO;
import com.condigence.nsuserservice.dto.UserDTO;
import com.condigence.nsuserservice.entity.Address;
import com.condigence.nsuserservice.entity.User;
import com.condigence.nsuserservice.service.UserService;
import com.condigence.nsuserservice.util.AppProperties;
import com.condigence.nsuserservice.util.CustomErrorType;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;


@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/neerseva/api")
public class UserController {

	public static final Logger logger = LoggerFactory.getLogger(UserController.class);

	@Autowired
	UserService service;

//	@Autowired
//	ImageService imageService;

	@Autowired
	public void setApp(AppProperties app) {
		this.app = app;
	}

	private AppProperties app;
	
	@Autowired
	UserService userService;
	
	@Autowired
	RestTemplate restTemplate;
	
	private static final String USER_SERVICE = "userService";
	
    private  static final String DEPARTMENT_SERVICE_URI= "/departments/";
	
	
//	@PostMapping("/")
//	public User saveUser(@RequestBody User user) {
//		return userService.saveUser(user);
//	}
	
	public ResponseEntity<?> userFallback(Exception exception) {
		return ResponseEntity.status(HttpStatus.OK).body("DEPARTMENT SERVICE IS DOWN! Please contact Admin!");
	}
	

//	@SuppressWarnings("unchecked")
//	@GetMapping("/{id}")
//	@CircuitBreaker(name=USER_SERVICE,fallbackMethod = "userFallback")
//	public ResponseEntity<?> getUserById(@PathVariable("id") long userId) {
//
//		String departmentURL= "http://NS-DEPARTMENT-SERVICE"+DEPARTMENT_SERVICE_URI;
//		Optional<User> user = userService.getById(4L);
//		if (user.isPresent()) {
////			ResponseTemplate responseTemplate = new ResponseTemplate();
////			UserDTO userDTO = new UserDTO();
////			userDTO.setUserId(user.get().getUserId());
////			userDTO.setUserName(user.get().getUserName());
//			DepartmentDTO departmentDTO = restTemplate.getForObject(departmentURL+user.get().getDepartmentId(), DepartmentDTO.class);
////			responseTemplate.setUserDTO(userDTO);
////			responseTemplate.setDepartmentDTO(departmentDTO);
//			return ResponseEntity.status(HttpStatus.OK).body(responseTemplate);
//		} else {
//			return new ResponseEntity(new CustomErrorType(" User not found with id : "+userId+" ",HttpStatus.NOT_FOUND.toString()), HttpStatus.NOT_FOUND);
//		}
//
//	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@PostMapping(value = "/v1/users")
	public ResponseEntity<?> addUsers(@RequestBody UserDTO dto) {
		logger.info("Entering addUsers with user Details >>>>>>>>  : {}", dto);
		HttpHeaders headers = new HttpHeaders();

		// Check If User Already Exists
		Optional<User> user = service.findByContact(dto.getContact());
		if (user.isPresent()) {
			return new ResponseEntity(new CustomErrorType("User Already Registered!"), HttpStatus.CONFLICT);
		} else {
			// New User
			User savedUser = service.save(dto);
			if (savedUser == null) {
				return new ResponseEntity(new CustomErrorType("Issue while saving User! Please contact Admin!"),
						HttpStatus.INTERNAL_SERVER_ERROR);
			} else {
				return new ResponseEntity<String>(headers, HttpStatus.CREATED);
			}

		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@PostMapping(value = "/v1/verify/login", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> verifyLogin(@RequestBody UserDTO dto) {
		logger.info("Entering login with user Details >>>>>>>>  : {}", dto);
		// HttpHeaders headers = new HttpHeaders();

		// Check If User contact Not Provided
		if (dto.getContact() == null || dto.getContact().trim().isEmpty()) {
			return new ResponseEntity(new CustomErrorType("Please provide contact!"), HttpStatus.NOT_FOUND);
		}
		// Verify Contact
		Optional<User> user = service.findByContact(dto.getContact());
		if (user.isPresent()) {
			return new ResponseEntity(user.get(), HttpStatus.OK);
		} else {
			logger.info("User Not Found for contact {}", dto.getContact());
			return new ResponseEntity(new CustomErrorType("Contact Not Found! Please Register"), HttpStatus.NOT_FOUND);
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@PostMapping(value = "/v1/verify/registration", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> verifyRegistration(@RequestBody UserDTO dto) {
		logger.info("Entering Verify User with user Details >>>>>>>>  : {}", dto);
		// HttpHeaders headers = new HttpHeaders();
		// Verify Contact
		Optional<User> user = service.findByContact(dto.getContact());
		// System.out.println(user.get().getContact());
		if (user.isPresent()) {
			return new ResponseEntity(new CustomErrorType("User Already Registered!"), HttpStatus.CONFLICT);

		} else {
			return new ResponseEntity(dto, HttpStatus.OK);
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@PostMapping(value = "/v1/verify/otp")
	public ResponseEntity<?> verifyOTP(@RequestBody UserDTO dto) {
		logger.info("Entering otp with user Details >>>>>>>>  : {}", dto);
		// HttpHeaders headers = new HttpHeaders();
		logger.debug("Inside verifyOTP with contact {}", dto.getContact());

		Optional<User> user = service.findByContact(dto.getContact());
		if (user.isPresent()) {
			logger.debug("User present for contact {}", dto.getContact());
			if (user.get().getOtp().equalsIgnoreCase(dto.getOtp())) {
				logger.debug("OTP Match for contact {}", dto.getContact());
				///TODO :
				User u = user.get();
				if(u.getIsActive() != null && u.getIsActive().equalsIgnoreCase("Y")) {
					dto.setActive(true);
				}else {
					dto.setActive(false);
				}
				//
				//dto.setAddressId(u.getAddressId());
				dto.setContact(u.getContact());
				//dto.setDescription(u.getDescription());
				if(u.getEmail() != null && !u.getEmail().trim().isEmpty()) {
					dto.setEmail(u.getEmail());
				}
				dto.setId(u.getId());

				if (u.getImageId() != null) {
					dto.setImageId(u.getImageId());
					byte[] pic = fetchImagePic(u.getImageId());
					if (pic != null) dto.setPic(pic);
				}

				dto.setName(u.getName());
				dto.setType(u.getType());
				dto.setOtp("");

				return new ResponseEntity(dto, HttpStatus.OK);
			} else {
				logger.info("OTP did not Match for contact {}", dto.getContact());
				return new ResponseEntity(new CustomErrorType("Sorry, Invalid OTP. Try again!"), HttpStatus.NOT_FOUND);
			}

		} else {
			logger.info("User Not present for contact {}", dto.getContact());
			return new ResponseEntity(new CustomErrorType("Sorry, Contact Admin!"), HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

//	@SuppressWarnings({ "unchecked", "rawtypes" })
//	@PostMapping(value = "/v1/verify")
//	public ResponseEntity<?> verifyUser(@RequestBody UserDTO dto) {
//		logger.info("Entering verifyUser with user Details >>>>>>>>  : {}", dto);
//		// HttpHeaders headers = new HttpHeaders();
//		System.out.println("Inside verify User with contact " + dto.getContact());
//
//		Optional<User> user = service.findByContact(dto.getContact());
//		if (user.isPresent()) {
//			System.out.println("User present");
//			if (user.get().getOtp().equalsIgnoreCase(dto.getOtp())) {
//				System.out.println("OTP Match");
//				return new ResponseEntity(user.get(), HttpStatus.OK);
//			} else {
//				System.out.println("OTP did not Match");
//				return new ResponseEntity(new CustomErrorType("Sorry, Invalid OTP. Try again!"), HttpStatus.NOT_FOUND);
//			}
//
//		} else {
//			System.out.println("User Not present");
//			return new ResponseEntity(new CustomErrorType("Sorry, Contact Admin!"), HttpStatus.INTERNAL_SERVER_ERROR);
//		}
//
//	}



	@GetMapping("/v1/users")
	public ResponseEntity<?> getAllUsers() {

		List<UserDTO> dtos = new ArrayList<>();
		List<User> users = null;
		users = service.getAll();

		if (users.size() == 0) {
			return new ResponseEntity(new CustomErrorType("Users Not Found! Please Register"), HttpStatus.NOT_FOUND);
		}

		for (User user : users) {
			UserDTO dto = new UserDTO();
			dto.setId(user.getId());
			dto.setContact(user.getContact());
			dto.setName(user.getName());
			dto.setEmail(user.getEmail());
			dto.setType(user.getType());
			if (user.getImageId() != null) {
				dto.setImageId(user.getImageId());
				logger.debug("Fetching image for user id {} imageId {}", user.getId(), user.getImageId());
				byte[] pic = fetchImagePic(user.getImageId());
				if (pic != null) dto.setPic(pic);
			}
			dtos.add(dto);
		}
		return ResponseEntity.status(HttpStatus.OK).body(dtos);

	}

	@GetMapping("/v1/users/{id:\\d+}")
	public ResponseEntity<?> getByUserId(@PathVariable("id") Long id) {
		//System.out.println("Inside getByUserId" + id);
		Optional<User> user = service.getById(id);
		UserDTO dto = new UserDTO();
		if (user.isPresent()) {
			dto.setId(user.get().getId());
			dto.setName(user.get().getName());
			dto.setEmail(user.get().getEmail());
			dto.setEmail(user.get().getEmail());
			dto.setContact(user.get().getContact());
			dto.setType(user.get().getType());
			if (user.get().getIsActive().equalsIgnoreCase("Y")) {
				dto.setActive(true);
			}
			dto.setDescription(user.get().getDescription());
			dto.setAddressId(user.get().getAddressId());
			if (user.get().getImageId() != null) {
				dto.setImageId(user.get().getImageId());
				logger.debug("Fetching image for user id {} imageId {}", user.get().getId(), user.get().getImageId());
				byte[] pic = fetchImagePic(user.get().getImageId());
				if (pic != null) dto.setPic(pic);
			}
			return ResponseEntity.status(HttpStatus.OK).body(dto);

		} else {
			return new ResponseEntity(new CustomErrorType("Profile not found."), HttpStatus.NOT_FOUND);
		}

	}

//	private Image getPicById(long imageId) {
//
//		String imagePath = app.getLocation();
//		return imageService.getImage(imageId, imagePath);
//	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@PutMapping(value = "/v1/users")
	public ResponseEntity<?> updateUser(@RequestBody UserDTO dto) {
		logger.info("Updating User  with id {}", dto.getId());

		String isProfileCompleted = "N";
		logger.debug("Update payload: {}", dto);

		Optional<User> user = service.getById(dto.getId());

		if (!user.isPresent()) {
			logger.error("Unable to update. Profile with id {} not found.", dto.getId());
			return new ResponseEntity(
					new CustomErrorType("Unable to upate. Profile with id " + dto.getId() + " not found."),
					HttpStatus.NOT_FOUND);
		} else {
			if(dto.getName() != null && !dto.getName().trim().isEmpty()) {
				user.get().setName(dto.getName());
			}

			if(dto.getImageId() != null) {
				user.get().setImageId(dto.getImageId());
			}

			user.get().setIsActive("Y");

			user.get().setType(dto.getType());
			if(dto.getEmail() != null && !dto.getEmail().trim().isEmpty()) {
				user.get().setEmail(dto.getEmail());
			}

			if(dto.getContact() != null && !dto.getContact().trim().isEmpty()) {
				user.get().setContact(dto.getContact());
			}


			service.update(user.get());
			return new ResponseEntity<User>(user.get(), HttpStatus.OK);
		}

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@DeleteMapping(value = "/v1/users/{id:\\d+}")
	public ResponseEntity<?> deleteUsers(@PathVariable("id") long id) {
		logger.info("Fetching & Deleting Users with id {}", id);
		Optional<User> user = service.getById(id);
		if (user.isPresent()) {
			service.deleteById(id);
		} else {
			logger.error("Unable to delete. User with id {} not found.", id);
			return new ResponseEntity(new CustomErrorType("Unable to delete. User with id " + id + " not found."),
					HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<User>(HttpStatus.OK);
	}

	//////////////////// Address /////////


	@SuppressWarnings({ "unchecked", "rawtypes" })
	@DeleteMapping(value = "/v1/addresses/{id:\\d+}")
	public ResponseEntity<?> deleteAddressById(@PathVariable("id") long id) {
		logger.info("Fetching & Deleting Users Address with id {}", id);
		service.deleteAddressById(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}


	@SuppressWarnings({ "unchecked", "rawtypes" })
	@PostMapping(value = "/v1/address")
	public ResponseEntity<?> addUserAddress(@RequestBody AddressDTO dto) {
		logger.info("Entering addUserAddress with Details >>>>>>>>  : {}", dto);
		HttpHeaders headers = new HttpHeaders();
		Address address = service.saveAddress(dto);
		return new ResponseEntity<Address>(address, HttpStatus.CREATED);

	}

	@GetMapping("/v1/addresses/by/user/{id:\\d+}")
	public ResponseEntity<?> getAllUserAddressesById(@PathVariable("id") long id) {
		List<AddressDTO> dtos = new ArrayList<>();
		List<Address> addresses = service.getAllAddressesByUserId(id);
		for (Address address : addresses) {
			AddressDTO dto = new AddressDTO();
			dto.setId(address.getId());
			dto.setLine1(address.getLine1());

			dto.setLine2(address.getLine2());
			dto.setLine3(address.getLine3());
			dto.setLine4(address.getLine4());

			dto.setCity(address.getCity());
			dto.setState(address.getState());
			dto.setCountry(address.getCountry());

			dto.setType(address.getType());
			dto.setUserId(address.getUserId());
			dto.setIsDefault(address.getIsDefault());
			dtos.add(dto);

		}
		return ResponseEntity.status(HttpStatus.OK).body(dtos);
	}

	public List<Address> getAllUserAddresses(Long id) {
		List<Address> addresses = service.getAllAddressesByUserId(id);
		return addresses;

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@PutMapping(value = "/v1/makeDefault")
	public ResponseEntity<?> makeDefault(@RequestBody AddressDTO dto) {
		logger.info("Updating Address  with id {}", dto.getId());
		List<Address> address = (List<Address>) getAllUserAddresses(dto.getUserId());
		for (Address add : address) {
			logger.debug("Checking address id {} default {}", add.getId(), add.getIsDefault());
			if (add.getId() == dto.getId() && add.getIsDefault().equalsIgnoreCase("N") ) {
				add.setIsDefault("Y");
			} else {
				add.setIsDefault("N");
			}
			service.updateAddress(add);
		}
		return new ResponseEntity<Address>(HttpStatus.OK);
	}

	@GetMapping("/v1/getDefault/addresses/by/user/{id:\\d+}")
	public ResponseEntity<?> getDefaultAddressesById(@PathVariable("id") long id) {
		AddressDTO dto = new AddressDTO();

		List<Address> addresses = service.getAllAddressesByUserId(id);
		for (Address address : addresses) {
			if (address.getIsDefault().equalsIgnoreCase("Y")) {
				dto.setId(address.getId());
				dto.setLine1(address.getLine1());
				dto.setType(address.getType());
				dto.setUserId(address.getUserId());
				dto.setIsDefault(address.getIsDefault());
			}
		}
		return ResponseEntity.status(HttpStatus.OK).body(dto);
	}

	@GetMapping("/v1/addresses/by/{id:\\d+}")
	public ResponseEntity<?> getAddressesById(@PathVariable("id") long id) {
		AddressDTO dto = new AddressDTO();

		Optional<Address> address = service.getAddressesById(id);

		if (address.isPresent()) {
			dto.setId(address.get().getId());
			dto.setLine1(address.get().getLine1());

			dto.setLine2(address.get().getLine2());
			dto.setLine3(address.get().getLine3());
			dto.setLine4(address.get().getLine4());

			dto.setCity(address.get().getCity());
			dto.setState(address.get().getState());
			dto.setCountry(address.get().getCountry());

			dto.setType(address.get().getType());
			dto.setUserId(address.get().getUserId());
			dto.setIsDefault(address.get().getIsDefault());
		}

		return ResponseEntity.status(HttpStatus.OK).body(dto);
	}

	@GetMapping("/v1/users/active/count")
	public ResponseEntity<Long> countActiveUsers() {
		logger.info("Counting active users");
		long activeCount = service.countActiveUsers();
		return ResponseEntity.ok(activeCount);
	}

	// New endpoint: aggregated counts
	@GetMapping("/v1/users/counts")
	public ResponseEntity<Map<String, Long>> getUserCounts() {
		logger.info("Getting user counts (total, active, customers, vendors)");
		long total = service.getAll().size();
		long active = service.countActiveUsers();
		long customers = service.countByType("CUSTOMER");
		long vendors = service.countByType("VENDOR");
		Map<String, Long> result = new HashMap<>();
		result.put("total", total);
		result.put("active", active);
		result.put("customers", customers);
		result.put("vendors", vendors);
		return ResponseEntity.ok(result);
	}

	// Simple in-memory cache: imageId -> pic (byte[])
	private final ConcurrentHashMap<Long, CachedImage> imageCache = new ConcurrentHashMap<>();

	// TTL for cache entries (milliseconds)
	private final long IMAGE_CACHE_TTL_MS = TimeUnit.MINUTES.toMillis(5);

	private static class CachedImage {
		final byte[] pic;
		final long ts;
		CachedImage(byte[] pic, long ts) { this.pic = pic; this.ts = ts; }
	}

	// Circuit-breaker-protected fetch method. If it fails, fallback to imageFallback.
	@CircuitBreaker(name = "imageService", fallbackMethod = "imageFallback")
	private byte[] fetchImagePicWithCircuit(Long imageId) {
		// First, check cache
		if (imageId == null) return null;
		CachedImage ci = imageCache.get(imageId);
		if (ci != null && (System.currentTimeMillis() - ci.ts) < IMAGE_CACHE_TTL_MS) {
			return ci.pic;
		}
		// Not cached or stale: fetch from remote
		ImageDTO imageDTO = restTemplate.getForObject("http://NS-IMAGE-SERVICE/neerseva/api/v1/images/" + imageId+"/data", ImageDTO.class);
		if (imageDTO == null || imageDTO.getPic() == null) {
			logger.warn("Image service returned null or empty pic for imageId {}", imageId);
			throw new RestClientException("Image empty for id " + imageId);
		}
		byte[] pic = imageDTO.getPic();
		imageCache.put(imageId, new CachedImage(pic, System.currentTimeMillis()));
		return pic;
	}

	// Fallback when circuit breaker trips or fetch throws
	@SuppressWarnings("unused")
	private byte[] imageFallback(Long imageId, Throwable t) {
		logger.warn("Image service unavailable or failed for imageId {}: {}. Using fallback image.", imageId, t == null ? "unknown" : t.getMessage());
		// Try cache one last time (even if stale)
		CachedImage ci = imageCache.get(imageId);
		if (ci != null) return ci.pic;
		// Use configured fallback image if present (assume base64 encoded)
		if (app != null && app.getFallbackImage() != null && !app.getFallbackImage().trim().isEmpty()) {
			try {
				return Base64.getDecoder().decode(app.getFallbackImage());
			} catch (IllegalArgumentException ex) {
				logger.warn("Configured fallbackImage is not valid base64: {}", ex.getMessage());
				return null;
			}
		}
		// else return null so controller omits pic
		return null;
	}

	// Keep a simple safe wrapper for old code to call: it uses the circuit-protected method and handles exceptions
	private byte[] fetchImagePic(Long imageId) {
		try {
			return fetchImagePicWithCircuit(imageId);
		} catch (Exception ex) {
			logger.warn("fetchImagePic encountered an error for imageId {}: {}", imageId, ex.getMessage());
			// fallback: try reading fallback from app
			if (app != null && app.getFallbackImage() != null && !app.getFallbackImage().trim().isEmpty()) {
				try {
					return Base64.getDecoder().decode(app.getFallbackImage());
				} catch (IllegalArgumentException e) {
					logger.warn("Configured fallbackImage is not valid base64: {}", e.getMessage());
					return null;
				}
			}
			return null;
		}
	}

}

package com.condigence.neerseva.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.condigence.neerseva.dto.AddressDTO;
import com.condigence.neerseva.dto.UserDTO;
import com.condigence.neerseva.entity.Address;
import com.condigence.neerseva.entity.Image;
import com.condigence.neerseva.entity.Stock;
import com.condigence.neerseva.entity.User;
import com.condigence.neerseva.services.ImageService;
import com.condigence.neerseva.services.UserService;
import com.condigence.neerseva.util.AppProperties;
import com.condigence.neerseva.util.CustomErrorType;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/neerseva/api")
public class UserController {

	public static final Logger logger = LoggerFactory.getLogger(UserController.class);

	@Autowired
	UserService service;

	@Autowired
	ImageService imageService;

	@Autowired
	public void setApp(AppProperties app) {
		this.app = app;
	}

	private AppProperties app;

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
		if (dto.getContact() == null || dto.getContact() == "") {
			return new ResponseEntity(new CustomErrorType("Please provide contact!"), HttpStatus.NOT_FOUND);
		}
		// Verify Contact
		Optional<User> user = service.findByContact(dto.getContact());
		if (user.isPresent()) {
			return new ResponseEntity(user.get(), HttpStatus.OK);
		} else {
			System.out.println("User Not Found!");
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
		System.out.println("Inside verifyOTP with contact " + dto.getContact());

		Optional<User> user = service.findByContact(dto.getContact());
		if (user.isPresent()) {
			System.out.println("User present");
			if (user.get().getOtp().equalsIgnoreCase(dto.getOtp())) {
				System.out.println("OTP Match");
				///TODO :
				User u = user.get();
				if(u.getIsActive().equalsIgnoreCase("Y")) {
					dto.setActive(true);
				}else {
					dto.setActive(false);
				}
				//
				//dto.setAddressId(u.getAddressId());
				dto.setContact(u.getContact());
				//dto.setDescription(u.getDescription());
				if(!u.getEmail().equalsIgnoreCase("")) {
					dto.setEmail(u.getEmail());
				}
				dto.setId(u.getId());
				
				if (u.getImageId() != null) {
					dto.setImageId(u.getImageId());
					dto.setPic(getPicById(u.getImageId()).getPic());
				}
				
				dto.setName(u.getName());
				dto.setType(u.getType());
				dto.setOtp("");
				
				return new ResponseEntity(dto, HttpStatus.OK);
			} else {
				System.out.println("OTP did not Match");
				return new ResponseEntity(new CustomErrorType("Sorry, Invalid OTP. Try again!"), HttpStatus.NOT_FOUND);
			}

		} else {
			System.out.println("User Not present");
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
				dto.setPic(getPicById(user.getImageId()).getPic());
			}
			dtos.add(dto);
		}
		return ResponseEntity.status(HttpStatus.OK).body(dtos);

	}

	@GetMapping("/v1/users/{id}")
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
			if (user.get().getIsActive().equalsIgnoreCase("Y")) {
				dto.setActive(true);
			}
			dto.setDescription(user.get().getDescription());
			dto.setAddressId(user.get().getAddressId());
			if (user.get().getImageId() != null) {
				dto.setImageId(user.get().getImageId());
				dto.setPic(getPicById(user.get().getImageId()).getPic());
			}
			return ResponseEntity.status(HttpStatus.OK).body(dto);

		} else {
			return new ResponseEntity(new CustomErrorType("Profile not found."), HttpStatus.NOT_FOUND);
		}

	}

	private Image getPicById(long imageId) {

		String imagePath = app.getLocation();
		return imageService.getImage(imageId, imagePath);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@PutMapping(value = "/v1/user")
	public ResponseEntity<?> updateUser(@RequestBody UserDTO dto) {
		logger.info("Updating User  with id {}", dto.getId());

		String isProfileCompleted = "N";
		System.out.println(dto);

		Optional<User> user = service.getById(dto.getId());

		System.out.println(user);

		if (!user.isPresent()) {
			logger.error("Unable to update. Profile with id {} not found.", dto.getId());
			return new ResponseEntity(
					new CustomErrorType("Unable to upate. Profile with id " + dto.getId() + " not found."),
					HttpStatus.NOT_FOUND);
		} else {
			if(!dto.getName().equalsIgnoreCase("")) {
				user.get().setName(dto.getName());
			}
			
			if(dto.getImageId() != null) {
				user.get().setImageId(dto.getImageId());
			}
			
			user.get().setIsActive("Y");
			
			if(!dto.getEmail().equalsIgnoreCase("")) {
				user.get().setEmail(dto.getEmail());
			}
			
			if(!dto.getContact().equalsIgnoreCase("")) {
				user.get().setContact(dto.getContact());
			}
			
			
			service.update(user.get());
			return new ResponseEntity<User>(user.get(), HttpStatus.OK);
		}

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@DeleteMapping(value = "/v1/users/{id}")
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
		return new ResponseEntity<Stock>(HttpStatus.OK);
	}

	//////////////////// Address /////////
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@DeleteMapping(value = "/v1/addresses/{id}")
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

	@GetMapping("/v1/addresses/by/user/{id}")
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
			System.out.println(add);
			if (add.getId() == dto.getId() && add.getIsDefault().equalsIgnoreCase("N") ) {
				add.setIsDefault("Y");
			} else {
				add.setIsDefault("N");
			}
			service.updateAddress(add);
		}
		return new ResponseEntity<Address>(HttpStatus.OK);
	}

	@GetMapping("/v1/getDefault/addresses/by/user/{id}")
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
	
	
	@GetMapping("/v1/addresses/by/{id}")
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
	
	
	
	////////////////////// vendors /////////////////////
	
	
	@GetMapping("/v1/vendors")
	public ResponseEntity<?> getAllVendors() {

		List<UserDTO> dtos = new ArrayList<>();
		List<User> users = null;
		users = service.getAll();

		if (users.size() == 0) {
			return new ResponseEntity(new CustomErrorType("Users Not Found! Please Register"), HttpStatus.NOT_FOUND);
		}
		int count = 0;

		for (User user : users) {
			if(user.getType() != null && user.getType().equalsIgnoreCase("VENDOR") && count !=5) {
				UserDTO dto = new UserDTO();
				dto.setId(user.getId());
				dto.setContact(user.getContact());
				dto.setName(user.getName());
				dto.setEmail(user.getEmail());
				dto.setType(user.getType());
				if (user.getImageId() != null) {
					dto.setImageId(user.getImageId());
					dto.setPic(getPicById(user.getImageId()).getPic());
				}
				dtos.add(dto);
				count ++;
			}
			
		}
		return ResponseEntity.status(HttpStatus.OK).body(dtos);

	}

}

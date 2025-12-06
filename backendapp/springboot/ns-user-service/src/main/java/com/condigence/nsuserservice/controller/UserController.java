package com.condigence.nsuserservice.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Comparator;
import java.util.stream.Collectors;

import com.condigence.nsuserservice.dto.AddressDTO;
import com.condigence.nsuserservice.dto.UserDTO;
import com.condigence.nsuserservice.entity.Address;
import com.condigence.nsuserservice.entity.User;
import com.condigence.nsuserservice.exception.BusinessException;
import com.condigence.nsuserservice.exception.ErrorCode;
import com.condigence.nsuserservice.service.UserService;
import com.condigence.nsuserservice.service.ImageService;
import com.condigence.nsuserservice.util.AppProperties;
import com.condigence.nsuserservice.util.CustomErrorType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/neerseva/api")
@Tag(name = "Users", description = "User and address management APIs")
public class UserController {

    public static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService service;

    @Autowired
    private ImageService imageService; // Dedicated connector for image microservice

    // Keep AppProperties wiring in case other methods/config rely on it later
    private AppProperties app;

    @Autowired
    public void setApp(AppProperties app) {
        this.app = app;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @PostMapping(value = "/v1/users")
    @Operation(summary = "Register new user", description = "Register a new user if the contact number is not already registered")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "User registered successfully"),
            @ApiResponse(responseCode = "409", description = "User already registered", content = @Content(schema = @Schema(implementation = CustomErrorType.class))),
            @ApiResponse(responseCode = "500", description = "Error while saving user", content = @Content(schema = @Schema(implementation = CustomErrorType.class)))
    })
    public ResponseEntity<?> addUsers(@RequestBody UserDTO dto) {
        logger.info("Entering addUsers with user Details >>>>>>>>  : {}", dto);
        HttpHeaders headers = new HttpHeaders();

        Optional<User> user = service.findByContact(dto.getContact());
        if (user.isPresent()) {
            // Use standardized business exception + error code
            throw new BusinessException(ErrorCode.USER_ALREADY_REGISTERED, "User Already Registered!");
        }

        User savedUser = service.save(dto);
        if (savedUser == null) {
            // Keep behavior via generic 500 but mark as technical in service or generic handler
            throw new BusinessException(ErrorCode.INTERNAL_ERROR, "Issue while saving User! Please contact Admin!");
        }
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @PostMapping(value = "/v1/verify/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Verify login", description = "Verify user login with contact. For CUSTOMER type, performs quick registration if user does not exist.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login verified"),
            @ApiResponse(responseCode = "400", description = "Contact missing", content = @Content(schema = @Schema(implementation = CustomErrorType.class))),
            @ApiResponse(responseCode = "404", description = "Contact not found", content = @Content(schema = @Schema(implementation = CustomErrorType.class)))
    })
    public ResponseEntity<?> verifyLogin(@RequestBody UserDTO dto) {
        logger.info("Entering login with user Details >>>>>>>>  : {}", dto.getContact());

        // Quick-register only when type is explicitly CUSTOMER
        if (dto.getType() != null && dto.getType().equalsIgnoreCase("CUSTOMER")) {
            if (dto.getContact() == null || dto.getContact().trim().isEmpty()) {
                throw new BusinessException(ErrorCode.CONTACT_MISSING, "Please provide contact!");
            }
            User user = quickRegisterMode(dto);
            return new ResponseEntity(user, HttpStatus.OK);
        } else {
            // Normal flow: contact is mandatory
            if (dto.getContact() == null || dto.getContact().trim().isEmpty()) {
                throw new BusinessException(ErrorCode.CONTACT_MISSING, "Please provide contact!");
            }
            Optional<User> user = service.findByContact(dto.getContact());
            if (user.isPresent()) {
                return new ResponseEntity(user.get(), HttpStatus.OK);
            } else {
                logger.info("User Not Found for contact {}", dto.getContact());
                throw new BusinessException(ErrorCode.USER_NOT_FOUND, "Contact Not Found! Please Register");
            }
        }
    }

    /**
     * Quick register flow for CUSTOMER: if user exists for contact, return existing; otherwise create a new CUSTOMER user.
     */
    private User quickRegisterMode(UserDTO dto) {
        Optional<User> existing = service.findByContact(dto.getContact());
        if (existing.isPresent()) {
            logger.info("Quick register: existing user found for contact {}", dto.getContact());
            return existing.get();
        }
        if (dto.getType() == null || dto.getType().trim().isEmpty()) {
            dto.setType("CUSTOMER");
        }
        return service.saveCustomer(dto);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @PostMapping(value = "/v1/verify/registration", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Verify registration", description = "Verify if user with provided contact is already registered")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User can register"),
            @ApiResponse(responseCode = "409", description = "User already registered", content = @Content(schema = @Schema(implementation = CustomErrorType.class)))
    })
    public ResponseEntity<?> verifyRegistration(@RequestBody UserDTO dto) {
        logger.info("Entering Verify User with user Details >>>>>>>>  : {}", dto);
        Optional<User> user = service.findByContact(dto.getContact());
        if (user.isPresent()) {
            throw new BusinessException(ErrorCode.USER_ALREADY_REGISTERED, "User Already Registered!");
        }
        return new ResponseEntity(dto, HttpStatus.OK);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @PostMapping(value = "/v1/verify/otp")
    @Operation(summary = "Verify OTP", description = "Verify the OTP for the user identified by contact")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OTP verified, user details returned"),
            @ApiResponse(responseCode = "404", description = "Invalid OTP", content = @Content(schema = @Schema(implementation = CustomErrorType.class))),
            @ApiResponse(responseCode = "500", description = "User not present or internal error", content = @Content(schema = @Schema(implementation = CustomErrorType.class)))
    })
    public ResponseEntity<?> verifyOTP(@RequestBody UserDTO dto) {
        logger.info("Entering otp with user Details >>>>>>>>  : {}", dto);
        logger.debug("Inside verifyOTP with contact {}", dto.getContact());

        Optional<User> user = service.findByContact(dto.getContact());
        if (user.isPresent()) {
            logger.debug("User present for contact {}", dto.getContact());
            User u = user.get();
            if (u.getOtp() != null && u.getOtp().equalsIgnoreCase(dto.getOtp())) {
                logger.debug("OTP Match for contact {}", dto.getContact());

                if (u.getIsActive() != null && u.getIsActive().equalsIgnoreCase("Y")) {
                    dto.setActive(true);
                } else {
                    dto.setActive(false);
                }

                dto.setContact(u.getContact());
                if (u.getEmail() != null && !u.getEmail().trim().isEmpty()) {
                    dto.setEmail(u.getEmail());
                }
                dto.setId(u.getId());

                if (u.getImageId() != null) {
                    dto.setImageId(u.getImageId());
                    byte[] pic = imageService.getImage(u.getImageId());
                    if (pic != null) {
                        dto.setPic(pic);
                    }
                }

                dto.setName(u.getName());
                dto.setType(u.getType());
                dto.setOtp("");

                return new ResponseEntity(dto, HttpStatus.OK);
            } else {
                logger.info("OTP did not Match for contact {}", dto.getContact());
                throw new BusinessException(ErrorCode.INVALID_OTP, "Sorry, Invalid OTP. Try again!");
            }

        } else {
            logger.info("User Not present for contact {}", dto.getContact());
            throw new BusinessException(ErrorCode.USER_NOT_FOUND, "Sorry, Contact Admin!");
        }

    }

    @GetMapping("/v1/users")
    @Operation(summary = "Get all users", description = "Retrieve all registered users")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of users",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = UserDTO.class)))),
            @ApiResponse(responseCode = "404", description = "No users found", content = @Content(schema = @Schema(implementation = CustomErrorType.class)))
    })
    public ResponseEntity<?> getAllUsers() {

        List<UserDTO> dtos = new ArrayList<>();
        List<User> users = service.getAll();

        if (users.isEmpty()) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND, "Users Not Found! Please Register");
        }

        for (User user : users) {
            UserDTO dto = new UserDTO();
            dto.setId(user.getId());
            dto.setContact(user.getContact());
            dto.setName(user.getName());
            dto.setEmail(user.getEmail());
            dto.setType(user.getType());
            dto.setAddressId(user.getAddressId());

            if (user.getImageId() != null) {
                dto.setImageId(user.getImageId());
                logger.debug("Fetching image for user id {} imageId {}", user.getId(), user.getImageId());
                byte[] pic = imageService.getImage(user.getImageId());
                if (pic != null) {
                    dto.setPic(pic);
                }
            }
            dtos.add(dto);
        }
        return ResponseEntity.status(HttpStatus.OK).body(dtos);

    }

    @GetMapping("v1/users/customers/top/5")
    @Operation(summary = "Get top 5 customers", description = "Retrieve top 5 customers ordered by creation date")
    public ResponseEntity<?> gettop5Customers() {

        List<User> users = service.getAll();
        if (users.isEmpty()) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND, "No Users Found! Please Register");
        }

        List<User> top5Customers = users.stream()
                .filter(u -> "CUSTOMER".equals(u.getType()))
                .sorted(Comparator.comparing(User::getDateCreated,
                        Comparator.nullsLast(Comparator.naturalOrder())).reversed())
                .limit(5)
                .collect(Collectors.toList());

        List<UserDTO> dtos = new ArrayList<>();
        for (User user : top5Customers) {
            UserDTO dto = new UserDTO();
            dto.setId(user.getId());
            dto.setContact(user.getContact());
            dto.setName(user.getName());
            dto.setEmail(user.getEmail());
            dto.setType(user.getType());
            dto.setAddressId(user.getAddressId());

            if (user.getImageId() != null) {
                dto.setImageId(user.getImageId());
                byte[] pic = imageService.getImage(user.getImageId());
                if (pic != null) {
                    dto.setPic(pic);
                }
            }
            dtos.add(dto);
        }

        return ResponseEntity.status(HttpStatus.OK).body(dtos);
    }

    @GetMapping("v1/users/vendors/top/5")
    @Operation(summary = "Get top 5 vendors", description = "Retrieve top 5 vendors as per service logic")
    public ResponseEntity<?> getTop5Vendors() {
        List<User> top5Vendors = service.getTop5Vendors();

        if (top5Vendors == null || top5Vendors.isEmpty()) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND, "No Vendors Found! Please Register");
        }

        List<UserDTO> dtos = new ArrayList<>();
        for (User user : top5Vendors) {
            UserDTO dto = new UserDTO();
            dto.setId(user.getId());
            dto.setContact(user.getContact());
            dto.setName(user.getName());
            dto.setEmail(user.getEmail());
            dto.setType(user.getType());
            dto.setAddressId(user.getAddressId());

            if (user.getImageId() != null) {
                dto.setImageId(user.getImageId());
                byte[] pic = imageService.getImage(user.getImageId());
                if (pic != null) {
                    dto.setPic(pic);
                }
            }

            dtos.add(dto);
        }

        return ResponseEntity.status(HttpStatus.OK).body(dtos);
    }

    @GetMapping("/v1/users/{id:\\d+}")
    @Operation(summary = "Get user by id", description = "Retrieve user profile for given id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User found", content = @Content(schema = @Schema(implementation = UserDTO.class))),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content(schema = @Schema(implementation = CustomErrorType.class)))
    })
    public ResponseEntity<?> getByUserId(@Parameter(description = "User ID") @PathVariable("id") Long id) {
        Optional<User> user = service.getById(id);
        UserDTO dto = new UserDTO();
        if (user.isPresent()) {
            dto.setId(user.get().getId());
            dto.setName(user.get().getName());
            dto.setEmail(user.get().getEmail());
            dto.setContact(user.get().getContact());
            dto.setType(user.get().getType());
            if (user.get().getIsActive() != null && user.get().getIsActive().equalsIgnoreCase("Y")) {
                dto.setActive(true);
            }
            dto.setDescription(user.get().getDescription());
            dto.setAddressId(user.get().getAddressId());
            if (user.get().getImageId() != null) {
                dto.setImageId(user.get().getImageId());
                logger.debug("Fetching image for user id {} imageId {}", user.get().getId(), user.get().getImageId());
                byte[] pic = imageService.getImage(user.get().getImageId());
                if (pic != null) {
                    dto.setPic(pic);
                }
            }
            return ResponseEntity.status(HttpStatus.OK).body(dto);

        } else {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND, "Profile not found.");
        }

    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @PutMapping(value = "/v1/users")
    @Operation(summary = "Update user", description = "Update basic details of a user")
    public ResponseEntity<?> updateUser(@RequestBody UserDTO dto) {
        logger.info("Updating User  with id {}", dto.getId());
        logger.debug("Update payload: {}", dto);

        Optional<User> user = service.getById(dto.getId());

        if (user.isEmpty()) {
            logger.error("Unable to update. Profile with id {} not found.", dto.getId());
            throw new BusinessException(ErrorCode.USER_NOT_FOUND,
                    "Unable to upate. Profile with id " + dto.getId() + " not found.");
        } else {
            if (dto.getName() != null && !dto.getName().trim().isEmpty()) {
                user.get().setName(dto.getName());
            }

            if (dto.getImageId() != null) {
                user.get().setImageId(dto.getImageId());
            }

            user.get().setIsActive("Y");

            user.get().setType(dto.getType());
            if (dto.getEmail() != null && !dto.getEmail().trim().isEmpty()) {
                user.get().setEmail(dto.getEmail());
            }

            if (dto.getContact() != null && !dto.getContact().trim().isEmpty()) {
                user.get().setContact(dto.getContact());
            }

            service.update(user.get());
            return new ResponseEntity<User>(user.get(), HttpStatus.OK);
        }

    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @DeleteMapping(value = "/v1/users/{id:\\d+}")
    @Operation(summary = "Delete user", description = "Delete a user by id")
    public ResponseEntity<?> deleteUsers(@Parameter(description = "User ID") @PathVariable("id") long id) {
        logger.info("Fetching & Deleting Users with id {}", id);
        Optional<User> user = service.getById(id);
        if (user.isPresent()) {
            service.deleteById(id);
        } else {
            logger.error("Unable to delete. User with id {} not found.", id);
            throw new BusinessException(ErrorCode.USER_NOT_FOUND,
                    "Unable to delete. User with id " + id + " not found.");
        }
        return new ResponseEntity<User>(HttpStatus.OK);
    }

    //////////////////// Address /////////

    @DeleteMapping(value = "/v1/addresses/{id:\\d+}")
    @Operation(summary = "Delete address", description = "Delete address by id")
    public ResponseEntity<?> deleteAddressById(@Parameter(description = "Address ID") @PathVariable("id") long id) {
        logger.info("Fetching & Deleting Users Address with id {}", id);
        service.deleteAddressById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "/v1/address")
    @Operation(summary = "Add address", description = "Add a new address for a user")
    public ResponseEntity<?> addUserAddress(@RequestBody AddressDTO dto) {
        logger.info("Entering addUserAddress with Details >>>>>>>>  : {}", dto);
        Address address = service.saveAddress(dto);
        return new ResponseEntity<Address>(address, HttpStatus.CREATED);
    }

    @PutMapping(value = "/v1/addresses/by/{id:\\d+}")
    @Operation(summary = "Update address", description = "Update an existing address")
    public ResponseEntity<?> updateUserAddress(@RequestBody AddressDTO dto) {
        logger.info("Entering updateUserAddress with Details >>>>>>>>  : {}", dto);
        Address address = service.updateAddress(dto);
        return new ResponseEntity<Address>(address, HttpStatus.OK);
    }

    @GetMapping("/v1/addresses/by/user/{id:\\d+}")
    @Operation(summary = "Get all addresses for user", description = "Retrieve all addresses for a given user id")
    public ResponseEntity<?> getAllUserAddressesById(@Parameter(description = "User ID") @PathVariable("id") long id) {
        List<AddressDTO> dtos = new ArrayList<>();
        List<Address> addresses = service.getAllAddressesByUserId(id);

        // safe-null: if service returns null or empty, return empty list
        if (addresses == null || addresses.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(dtos);
        }

        for (Address address : addresses) {
            AddressDTO dto = new AddressDTO();
            dto.setId(address.getId());
            dto.setType(address.getType());
            dto.setLine1(address.getLine1());
            dto.setLine2(address.getLine2());
            dto.setLine3(address.getLine3());
            dto.setLine4(address.getLine4());
            dto.setPin(address.getPin());
            dto.setCity(address.getCity());
            dto.setState(address.getState());
            dto.setCountry(address.getCountry());
            dto.setUserId(address.getUserId());
            dto.setIsDefault(address.getIsDefault());

            // The Address entity currently does not expose location fields; initialize DTO fields explicitly
            dto.setLocationLong(null);
            dto.setLocationLatt(null);
            dto.setLocationName(null);

            dtos.add(dto);
        }
        return ResponseEntity.status(HttpStatus.OK).body(dtos);
    }

    public List<Address> getAllUserAddresses(Long id) {
        return service.getAllAddressesByUserId(id);
    }

    @PutMapping(value = "/v1/makeDefault")
    @Operation(summary = "Make default address", description = "Set the default address for a user")
    public ResponseEntity<?> makeDefault(@RequestBody AddressDTO dto) {
        logger.info("Updating Address  with id {}", dto.getId());
        List<Address> address = getAllUserAddresses(dto.getUserId());
        for (Address add : address) {
            logger.debug("Checking address id {} default {}", add.getId(), add.getIsDefault());
            if (add.getId() == dto.getId() && add.getIsDefault().equalsIgnoreCase("N")) {
                add.setIsDefault("Y");
            } else {
                add.setIsDefault("N");
            }
            service.updateAddress(add);
        }
        return new ResponseEntity<Address>(HttpStatus.OK);
    }

    @GetMapping("/v1/getDefault/addresses/by/user/{id:\\d+}")
    @Operation(summary = "Get default address for user", description = "Retrieve default address for a given user id")
    public ResponseEntity<?> getDefaultAddressesById(@Parameter(description = "User ID") @PathVariable("id") long id) {
        List<Address> addresses = service.getAllAddressesByUserId(id);

        if (addresses == null || addresses.isEmpty()) {
            throw new BusinessException(ErrorCode.ADDRESS_NOT_FOUND,
                    "No addresses found for user id " + id);
        }

        Optional<Address> defaultAddress = addresses.stream()
                .filter(a -> a.getIsDefault() != null && a.getIsDefault().equalsIgnoreCase("Y"))
                .findFirst();

        if (!defaultAddress.isPresent()) {
            throw new BusinessException(ErrorCode.ADDRESS_NOT_FOUND,
                    "Default address not found for user id " + id);
        }

        Address address = defaultAddress.get();
        AddressDTO dto = new AddressDTO();
        dto.setId(address.getId());
        dto.setType(address.getType());
        dto.setLine1(address.getLine1());
        dto.setLine2(address.getLine2());
        dto.setLine3(address.getLine3());
        dto.setLine4(address.getLine4());
        dto.setPin(address.getPin());
        dto.setCity(address.getCity());
        dto.setState(address.getState());
        dto.setCountry(address.getCountry());
        dto.setUserId(address.getUserId());
        dto.setIsDefault(address.getIsDefault());

        // The Address entity currently does not expose location fields; initialize DTO fields explicitly
        dto.setLocationLong(null);
        dto.setLocationLatt(null);
        dto.setLocationName(null);

        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }

    @GetMapping("/v1/addresses/by/{id:\\d+}")
    @Operation(summary = "Get address by id", description = "Retrieve address for given id")
    public ResponseEntity<?> getAddressesById(@Parameter(description = "Address ID") @PathVariable("id") long id) {
        Optional<Address> address = service.getAddressesById(id);

        if (address.isEmpty()) {
            throw new BusinessException(ErrorCode.ADDRESS_NOT_FOUND,
                    "Address not found for id " + id);
        }

        Address addr = address.get();
        AddressDTO dto = new AddressDTO();
        dto.setId(addr.getId());
        dto.setType(addr.getType());
        dto.setLine1(addr.getLine1());
        dto.setLine2(addr.getLine2());
        dto.setLine3(addr.getLine3());
        dto.setLine4(addr.getLine4());
        dto.setPin(addr.getPin());
        dto.setCity(addr.getCity());
        dto.setState(addr.getState());
        dto.setCountry(addr.getCountry());
        dto.setUserId(addr.getUserId());
        dto.setIsDefault(addr.getIsDefault());

        // The Address entity currently does not expose location fields; initialize DTO fields explicitly
        dto.setLocationLong(null);
        dto.setLocationLatt(null);
        dto.setLocationName(null);

        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }

    @GetMapping("/v1/users/active/count")
    @Operation(summary = "Count active users", description = "Get number of active users")
    public ResponseEntity<Long> countActiveUsers() {
        long activeCount = service.countActiveUsers();
        return ResponseEntity.ok(activeCount);
    }

    @GetMapping("/v1/users/counts")
    @Operation(summary = "Get user counts", description = "Get total, active, customers and vendors counts")
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


}

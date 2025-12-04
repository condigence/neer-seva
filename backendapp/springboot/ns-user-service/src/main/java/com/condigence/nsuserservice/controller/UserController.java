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

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/neerseva/api")
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
        logger.info("Entering login with user Details >>>>>>>>  : {}", dto.getContact());

        // Quick-register only when type is explicitly CUSTOMER
        if (dto.getType() != null && dto.getType().equalsIgnoreCase("CUSTOMER")) {
            if (dto.getContact() == null || dto.getContact().trim().isEmpty()) {
                return new ResponseEntity(new CustomErrorType("Please provide contact!"), HttpStatus.BAD_REQUEST);
            }
            User user = quickRegisterMode(dto);
            return new ResponseEntity(user, HttpStatus.OK);
        } else {
            // Normal flow: contact is mandatory
            if (dto.getContact() == null || dto.getContact().trim().isEmpty()) {
                return new ResponseEntity(new CustomErrorType("Please provide contact!"), HttpStatus.NOT_FOUND);
            }
            Optional<User> user = service.findByContact(dto.getContact());
            if (user.isPresent()) {
                return new ResponseEntity(user.get(), HttpStatus.OK);
            } else {
                logger.info("User Not Found for contact {}", dto.getContact());
                return new ResponseEntity(new CustomErrorType("Contact Not Found! Please Register"), HttpStatus.NOT_FOUND);
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
    public ResponseEntity<?> verifyRegistration(@RequestBody UserDTO dto) {
        logger.info("Entering Verify User with user Details >>>>>>>>  : {}", dto);
        Optional<User> user = service.findByContact(dto.getContact());
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
                return new ResponseEntity(new CustomErrorType("Sorry, Invalid OTP. Try again!"), HttpStatus.NOT_FOUND);
            }

        } else {
            logger.info("User Not present for contact {}", dto.getContact());
            return new ResponseEntity(new CustomErrorType("Sorry, Contact Admin!"), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/v1/users")
    public ResponseEntity<?> getAllUsers() {

        List<UserDTO> dtos = new ArrayList<>();
        List<User> users = service.getAll();

        if (users.isEmpty()) {
            return new ResponseEntity(new CustomErrorType("Users Not Found! Please Register"), HttpStatus.NOT_FOUND);
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
    public ResponseEntity<?> gettop5Customers() {

        List<User> users = service.getAll();
        if (users.isEmpty()) {
            return new ResponseEntity<>(new CustomErrorType("No Users Found! Please Register"), HttpStatus.NOT_FOUND);
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
    public ResponseEntity<?> getTop5Vendors() {
        List<User> top5Vendors = service.getTop5Vendors();

        if (top5Vendors == null || top5Vendors.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new CustomErrorType("No Vendors Found! Please Register"));
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
    public ResponseEntity<?> getByUserId(@PathVariable("id") Long id) {
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
            return new ResponseEntity(new CustomErrorType("Profile not found."), HttpStatus.NOT_FOUND);
        }

    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @PutMapping(value = "/v1/users")
    public ResponseEntity<?> updateUser(@RequestBody UserDTO dto) {
        logger.info("Updating User  with id {}", dto.getId());
        logger.debug("Update payload: {}", dto);

        Optional<User> user = service.getById(dto.getId());

        if (user.isEmpty()) {
            logger.error("Unable to update. Profile with id {} not found.", dto.getId());
            return new ResponseEntity(
                    new CustomErrorType("Unable to upate. Profile with id " + dto.getId() + " not found."),
                    HttpStatus.NOT_FOUND);
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

    @DeleteMapping(value = "/v1/addresses/{id:\\d+}")
    public ResponseEntity<?> deleteAddressById(@PathVariable("id") long id) {
        logger.info("Fetching & Deleting Users Address with id {}", id);
        service.deleteAddressById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "/v1/address")
    public ResponseEntity<?> addUserAddress(@RequestBody AddressDTO dto) {
        logger.info("Entering addUserAddress with Details >>>>>>>>  : {}", dto);
        Address address = service.saveAddress(dto);
        return new ResponseEntity<Address>(address, HttpStatus.CREATED);
    }

    @PutMapping(value = "/v1/addresses/by/{id:\\d+}")
    public ResponseEntity<?> updateUserAddress(@RequestBody AddressDTO dto) {
        logger.info("Entering updateUserAddress with Details >>>>>>>>  : {}", dto);
        Address address = service.updateAddress(dto);
        return new ResponseEntity<Address>(address, HttpStatus.OK);
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
        return service.getAllAddressesByUserId(id);
    }

    @PutMapping(value = "/v1/makeDefault")
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
        long activeCount = service.countActiveUsers();
        return ResponseEntity.ok(activeCount);
    }

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


}

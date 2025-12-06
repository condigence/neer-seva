package com.condigence.nsuserservice.service;


import com.condigence.nsuserservice.dto.AddressDTO;
import com.condigence.nsuserservice.dto.UserDTO;
import com.condigence.nsuserservice.entity.User;
import com.condigence.nsuserservice.entity.Address;
import com.condigence.nsuserservice.repository.AddressRepository;
import com.condigence.nsuserservice.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Service
public class UserService {

	@Autowired
	UserRepository repository;

	@Autowired
	AddressRepository addressRepository;

	public static final Logger logger = LoggerFactory.getLogger(UserRepository.class);

	public List<User> getAll() {
		return repository.findAll();
	}

	public Optional<User> getById(Long id) {
		return repository.findById(id);
	}

	public User save(UserDTO dto) {
		User user = new User();
		user.setName(dto.getName());
		user.setEmail(dto.getEmail());
		user.setType(dto.getType());
		user.setContact(dto.getContact());
		if (dto.getImageId() != null) {
			user.setImageId(dto.getImageId());
		}

		user.setIsActive("Y");
		user.setIsDeleted("N");
		user.setOtp("1234"); // By Default 1234 for Now
		return repository.save(user);
	}

    public User saveCustomer(UserDTO dto) {
        User user = new User();
        user.setType(dto.getType());
        user.setContact(dto.getContact());
        user.setIsActive("N");
        user.setIsDeleted("N");
        user.setOtp("1234"); // By Default 1234 for Now
        // populate creation timestamp for quick-registered customers
        user.setDateCreated(String.valueOf(LocalDateTime.now()));
        return repository.save(user);
    }

	public User update(User user) {
		return repository.save(user);
	}

	public Optional<User> findByContact(String contact) {
		return repository.findByContact(contact);
	}

	public void deleteById(long id) {
		repository.deleteById(id);
	}

	public Optional<User> verifyOTP(String contact, String otp) {
		return repository.findByOtp(otp);
	}

	/////////////////////////////

	public Address saveAddress(AddressDTO dto) {
		Address address = new Address();
		address.setType(dto.getType());
		address.setLine1(dto.getLine1());
		address.setLine2(dto.getLine2());
		address.setLine3(dto.getLine3());
		address.setLine4(dto.getLine4());
		address.setPin(dto.getPin());
		address.setCity(dto.getCity());
		address.setState(dto.getState());
		address.setCountry(dto.getState());
		if (dto.getIsDefault() != null && dto.getIsDefault().equalsIgnoreCase("true")) {
			List<Address> addresses = (List<Address>) getAllAddressesByUserId(dto.getUserId());
			for (Address add : addresses) {
				add.setIsDefault("N");

			}
			address.setIsDefault("Y");
		} else {
			address.setIsDefault("N");
		}

		address.setUserId(dto.getUserId());
		return addressRepository.save(address);
	}

	public List<Address> getAllAddresses() {
		return addressRepository.findAll();
	}

	public List<Address> getAllAddressesByUserId(Long id) {
		return addressRepository.findByUserId(id);
	}
	
	public Optional<Address> getAddressesById(Long id) {
		return addressRepository.findById(id);
	}

	public Address updateAddress(AddressDTO dto) {
        Address address = populateAddress(dto);
		return updateAddress(address);
	}

    public Address updateAddress(Address address) {
        return addressRepository.save(address);
    }

    private Address populateAddress(AddressDTO dto) {
        Address address = getAddressesById(dto.getId()).get();
        if (dto.getId() != null) {
            address.setId(dto.getId()); // allow updating existing by id
        }
        address.setType(dto.getType());
        address.setLine1(dto.getLine1());
        address.setLine2(dto.getLine2());
        address.setLine3(dto.getLine3());
        address.setLine4(dto.getLine4());
        address.setPin(dto.getPin());
        address.setCity(dto.getCity());
        address.setState(dto.getState());
        address.setCountry(dto.getCountry());
        address.setUserId(dto.getUserId());

        // Default flag mapping (normalize true/Y => Y else N)
        String def = dto.getIsDefault();
        if (def == null) {
            address.setIsDefault("N");
        } else if (def.equalsIgnoreCase("true") || def.equalsIgnoreCase("Y")) {
            address.setIsDefault("Y");
        } else {
            address.setIsDefault("N");
        }
        return address;
    }

    public void deleteAddressById(long id) {
		addressRepository.deleteById(id);
	}

    public Boolean addBalance(Long amount) {
		return true;
    }

    // New: count active users using repository
    public long countActiveUsers() {
        return repository.countByIsActive("Y");
    }

    // New: count users by type (case-insensitive)
    public long countByType(String type) {
        if (type == null) return 0L;
        return repository.countByTypeIgnoreCase(type);
    }

    // ✅ NEW METHOD: Get top 5 vendors
    public List<User> getTop5Vendors() {
        return repository.findTop5Vendors();
    }

}

package com.condigence.neerseva.services;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.condigence.neerseva.dto.AddressDTO;
import com.condigence.neerseva.dto.BrandDTO;
import com.condigence.neerseva.dto.UserDTO;
import com.condigence.neerseva.entity.Address;
import com.condigence.neerseva.entity.Brand;
import com.condigence.neerseva.entity.User;
import com.condigence.neerseva.repository.AddressRepository;
import com.condigence.neerseva.repository.BrandRepository;
import com.condigence.neerseva.repository.UserRepository;
import com.condigence.neerseva.util.ImageUtil;

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

	public Address updateAddress(Address address) {
		return addressRepository.save(address);
	}

	public void deleteAddressById(long id) {
		addressRepository.deleteById(id);
	}

}

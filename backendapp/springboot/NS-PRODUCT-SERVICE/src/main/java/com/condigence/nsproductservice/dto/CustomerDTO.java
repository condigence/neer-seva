package com.condigence.nsproductservice.dto;

import java.util.List;

public class CustomerDTO {

	
	private Long customerId;
	
	private String name;
	
	private String email;
	
	private String contact;
	
	private Long imageId;

	public Long getImageId() {
		return imageId;
	}

	public byte[] getPic() {
		return pic;
	}

	public void setImageId(Long imageId) {
		this.imageId = imageId;
	}

	public void setPic(byte[] pic) {
		this.pic = pic;
	}

	private byte[] pic;
	
	List<AddressDTO> addressList;

	public String getName() {
		return name;
	}

	public String getEmail() {
		return email;
	}

	public String getContact() {
		return contact;
	}

	public List<AddressDTO> getAddressList() {
		return addressList;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public void setAddressList(List<AddressDTO> addressList) {
		this.addressList = addressList;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

}

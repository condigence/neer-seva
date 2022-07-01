package com.condigence.nsorderservice.dto;

import java.util.Arrays;

public class UserDTO {

	private Long id;

	private String name;

	private String contact;

	private String email;

	private String otp;

	private String type;

	private String fullName;

	private Long imageId;

	private byte[] pic;

	private String description;

	private Long addressId;
	
	private boolean isActive;

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getContact() {
		return contact;
	}

	public String getOtp() {
		return otp;
	}

	public String getType() {
		return type;
	}

	public String getFullName() {
		return fullName;
	}

	public Long getImageId() {
		return imageId;
	}

	public byte[] getPic() {
		return pic;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getDescription() {
		return description;
	}

	public Long getAddressId() {
		return addressId;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public void setImageId(Long imageId) {
		this.imageId = imageId;
	}

	public void setPic(byte[] pic) {
		this.pic = pic;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setAddressId(Long addressId) {
		this.addressId = addressId;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	@Override
	public String toString() {
		return "UserDTO [id=" + id + ", name=" + name + ", contact=" + contact + ", email=" + email + ", otp=" + otp
				+ ", type=" + type + ", fullName=" + fullName + ", imageId=" + imageId + ", pic=" + Arrays.toString(pic)
				+ ", description=" + description + ", addressId=" + addressId + ", isActive=" + isActive + "]";
	}

	

}

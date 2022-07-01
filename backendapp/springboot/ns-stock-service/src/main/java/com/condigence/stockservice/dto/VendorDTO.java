package com.condigence.stockservice.dto;

import java.util.List;

public class VendorDTO {

	
	private Long vendorId;
	
	private String name;
	
	private String contact;
	
	private String email;
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	private Long imageId;
	
	List<ShopDTO> shopList;

	public List<ShopDTO> getShopList() {
		return shopList;
	}

	public void setShopList(List<ShopDTO> shopList) {
		this.shopList = shopList;
	}

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

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public Long getVendorId() {
		return vendorId;
	}

	public String getName() {
		return name;
	}

	public List<AddressDTO> getAddressList() {
		return addressList;
	}

	public void setVendorId(Long vendorId) {
		this.vendorId = vendorId;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setAddressList(List<AddressDTO> addressList) {
		this.addressList = addressList;
	}

	List<AddressDTO> addressList;

}

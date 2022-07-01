package com.condigence.nsproductservice.dto;

public class AddressDTO {

	private Long id;
	private String type;
	private String line1;
	private Long userId;
	private String isDefault;
	
	private String line2;
	private String line3;
	private String line4;

	private String pin;
	private String city;
	private String state;
	private String country;

	private String locationLong;
	private String locationLatt;
	private String locationName;
	public Long getId() {
		return id;
	}
	public String getType() {
		return type;
	}
	public String getLine1() {
		return line1;
	}
	public Long getUserId() {
		return userId;
	}
	public String getIsDefault() {
		return isDefault;
	}
	public String getLine2() {
		return line2;
	}
	public String getLine3() {
		return line3;
	}
	public String getLine4() {
		return line4;
	}
	public String getPin() {
		return pin;
	}
	public String getCity() {
		return city;
	}
	public String getState() {
		return state;
	}
	public String getCountry() {
		return country;
	}
	public String getLocationLong() {
		return locationLong;
	}
	public String getLocationLatt() {
		return locationLatt;
	}
	public String getLocationName() {
		return locationName;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public void setType(String type) {
		this.type = type;
	}
	public void setLine1(String line1) {
		this.line1 = line1;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public void setIsDefault(String isDefault) {
		this.isDefault = isDefault;
	}
	public void setLine2(String line2) {
		this.line2 = line2;
	}
	public void setLine3(String line3) {
		this.line3 = line3;
	}
	public void setLine4(String line4) {
		this.line4 = line4;
	}
	public void setPin(String pin) {
		this.pin = pin;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public void setState(String state) {
		this.state = state;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public void setLocationLong(String locationLong) {
		this.locationLong = locationLong;
	}
	public void setLocationLatt(String locationLatt) {
		this.locationLatt = locationLatt;
	}
	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}
	@Override
	public String toString() {
		return "AddressDTO [id=" + id + ", type=" + type + ", line1=" + line1 + ", userId=" + userId + ", isDefault="
				+ isDefault + ", line2=" + line2 + ", line3=" + line3 + ", line4=" + line4 + ", pin=" + pin + ", city="
				+ city + ", state=" + state + ", country=" + country + ", locationLong=" + locationLong
				+ ", locationLatt=" + locationLatt + ", locationName=" + locationName + "]";
	}

	


	
}

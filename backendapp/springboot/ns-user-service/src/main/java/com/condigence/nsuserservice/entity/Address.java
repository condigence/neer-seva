package com.condigence.nsuserservice.entity;

import javax.persistence.*;

@Entity
@Table(name = "address")
public class Address {

	public Address() {

	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private long id;

	@Column(name = "type")
	private String type;

	@Column(name = "line1")
	private String line1;

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

	@Column(name = "line2")
	private String line2;

	@Column(name = "line3")
	private String line3;
	
	@Column(name = "line4")
	private String line4;

	@Column(name = "pin")
	private String pin;
	
	@Column(name = "city")
	private String city;
	
	@Column(name = "state")
	private String state;
	
	@Column(name = "country")
	private String country;
//
//	private String locationLong;
//	private String locationLatt;
//	private String locationName;

	@Column(name = "userId")
	private Long userId;

	private String isDefault;

	public String getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(String isDefault) {
		this.isDefault = isDefault;
	}

	public long getId() {
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

	
	public void setId(long id) {
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

}

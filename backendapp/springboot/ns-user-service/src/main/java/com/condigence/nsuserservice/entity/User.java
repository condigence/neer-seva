package com.condigence.nsuserservice.entity;


import javax.persistence.*;

@Entity
@Table(name = "user")
public class User {

	public User() {
		super();
	}

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "name")
	private String name;

	@Column(name = "contact")
	private String contact;

	@Column(name = "email")
	private String email;
	
	@Column(name = "otp")
	private String otp;

	@Column(name = "type")
	private String type;

	@Column(name = "image_id")
	private Long imageId;

	@Column(name = "description")
	private String description;

	public Long getImageId() {
		return imageId;
	}

	public String getDescription() {
		return description;
	}

	public Long getAddressId() {
		return addressId;
	}

	public void setImageId(Long imageId) {
		this.imageId = imageId;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setAddressId(Long addressId) {
		this.addressId = addressId;
	}

	@Column(name = "address_id")
	private Long addressId;
	
	
	

	
//	@OneToMany(cascade = CascadeType.ALL)
//	@JoinTable(name = "user_address", joinColumns = @JoinColumn(name = "id"), inverseJoinColumns = @JoinColumn(name = "addressId"))
//	private List<Address> address;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Column(name = "is_deleted")
	private String isDeleted;

	@Column(name = "is_active")
	private String isActive;

	@Column(name = "date_created")
	private String dateCreated;

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getContact() {
		return contact;
	}

	public String getEmail() {
		return email;
	}

	public String getIsDeleted() {
		return isDeleted;
	}

	public String getDateCreated() {
		return dateCreated;
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

	public void setEmail(String email) {
		this.email = email;
	}

	public void setIsDeleted(String isDeleted) {
		this.isDeleted = isDeleted;
	}

	public void setDateCreated(String dateCreated) {
		this.dateCreated = dateCreated;
	}

	public String getOtp() {
		return otp;
	}

	public String getIsActive() {
		return isActive;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}
}

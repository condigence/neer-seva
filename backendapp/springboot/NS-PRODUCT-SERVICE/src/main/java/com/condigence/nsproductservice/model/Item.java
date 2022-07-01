package com.condigence.nsproductservice.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "item")
public class Item {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "name")
	private String name;

	@Column(name = "code")
	private String code;

	@Column(name = "price")
	private int price;

	@Column(name = "mrp")
	private Integer mrp;

	@Column(name = "dispPrice")
	private Integer dispPrice;

	@Column(name = "discount")
	private Integer discount;

	@Column(name = "type")
	private String type;

	@Column(name = "description")
	private String description;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "capacity")
	private Integer capacity;

	@Column(name = "brandId")
	private Long brandId;

	@Column(name = "imageId")
	private Long imageId;

	@Column(name = "dateCreated")
	private Date dateCreated;

	@Column(name = "quantity")
	private Integer quantity;

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getCode() {
		return code;
	}

	public Integer getPrice() {
		return price;
	}

	public Integer getMrp() {
		return mrp;
	}

	public Integer getDispPrice() {
		return dispPrice;
	}

	public Integer getDiscount() {
		return discount;
	}

	public String getType() {
		return type;
	}

	public Integer getCapacity() {
		return capacity;
	}

	public Long getImageId() {
		return imageId;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	public void setMrp(Integer mrp) {
		this.mrp = mrp;
	}

	public void setDispPrice(Integer dispPrice) {
		this.dispPrice = dispPrice;
	}

	public void setDiscount(Integer discount) {
		this.discount = discount;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setCapacity(Integer capcity) {
		this.capacity = capcity;
	}

	public void setImageId(Long imageId) {
		this.imageId = imageId;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Long getBrandId() {
		return brandId;
	}

	public void setBrandId(Long brandId) {
		this.brandId = brandId;
	}

}

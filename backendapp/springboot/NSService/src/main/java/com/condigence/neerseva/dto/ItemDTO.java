package com.condigence.neerseva.dto;

import java.util.Arrays;
import java.util.Date;

public class ItemDTO {

	private Long id;
	
	private byte[] pic;
	private String name;
	private Integer price;
	private Integer mrp;
	private Integer dispPrice;
	@Override
	public String toString() {
		return "ItemDTO [id=" + id + ", pic=" + Arrays.toString(pic) + ", name=" + name + ", price=" + price + ", mrp="
				+ mrp + ", dispPrice=" + dispPrice + ", quantity=" + quantity + ", code=" + code + ", discount="
				+ discount + ", type=" + type + ", description=" + description + ", capacity=" + capacity + ", brandId="
				+ brandId + ", imageId=" + imageId + ", dateCreated=" + dateCreated + "]";
	}

	private Integer quantity;
	private String code;
	private Integer discount;
	private String type;
	private String description;
	private Integer capacity;
	private Long brandId;
	private Long imageId;
	private Date dateCreated;

	public byte[] getPic() {
		return pic;
	}

	public void setPic(byte[] pic) {
		this.pic = pic;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	public Long getId() {
		return id;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public String getCode() {
		return code;
	}

	public String getName() {
		return name;
	}

	public Integer getMrp() {
		return mrp;
	}

	public Integer getDispPrice() {
		return dispPrice;
	}

	

	public String getType() {
		return type;
	}

	public String getDescription() {
		return description;
	}

	

	
	public Long getImageId() {
		return imageId;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setMrp(Integer mrp) {
		this.mrp = mrp;
	}

	public void setDispPrice(Integer dispPrice) {
		this.dispPrice = dispPrice;
	}

	

	public void setType(String type) {
		this.type = type;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	


	public void setImageId(Long imageId) {
		this.imageId = imageId;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	public Integer getCapacity() {
		return capacity;
	}

	public Long getBrandId() {
		return brandId;
	}

	public void setCapacity(Integer capacity) {
		this.capacity = capacity;
	}

	public void setBrandId(Long brandId) {
		this.brandId = brandId;
	}

	public Integer getDiscount() {
		return discount;
	}

	public void setDiscount(Integer discount) {
		this.discount = discount;
	}

}

package com.condigence.nsorderservice.dto;

public class ShopDTO {

	private long id;

	private String name;

	private Long imageId;
	
	private byte[] pic;

	public byte[] getPic() {
		return pic;
	}

	public void setPic(byte[] pic) {
		this.pic = pic;
	}

	private String type;

	private Long addressId;

	private Long userId;

	private String code;

	private String branch;

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Long getImageId() {
		return imageId;
	}

	public String getType() {
		return type;
	}

	public Long getAddressId() {
		return addressId;
	}

	public Long getUserId() {
		return userId;
	}

	public String getCode() {
		return code;
	}

	public String getBranch() {
		return branch;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setImageId(Long imageId) {
		this.imageId = imageId;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setAddressId(Long addressId) {
		this.addressId = addressId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}

	@Override
	public String toString() {
		return "ShopDTO [id=" + id + ", name=" + name + ", imageId=" + imageId + ", type=" + type + ", addressId="
				+ addressId + ", userId=" + userId + ", code=" + code + ", branch=" + branch + "]";
	}

	

}

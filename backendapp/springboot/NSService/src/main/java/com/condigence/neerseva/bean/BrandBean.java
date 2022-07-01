package com.condigence.neerseva.bean;

public class BrandBean {

	private String name;

	private Long imageId;

	public String getName() {
		return name;
	}

	public Long getImageId() {
		return imageId;
	}

	public Long getCreatedByUserId() {
		return createdByUserId;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setImageId(Long imageId) {
		this.imageId = imageId;
	}

	public void setCreatedByUserId(Long createdByUserId) {
		this.createdByUserId = createdByUserId;
	}

	private Long createdByUserId;

}

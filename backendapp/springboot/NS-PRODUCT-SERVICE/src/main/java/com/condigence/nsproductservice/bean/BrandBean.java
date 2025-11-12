package com.condigence.nsproductservice.bean;

import com.fasterxml.jackson.annotation.JsonSetter;

public class BrandBean {

	private String name;

	private Long imageId;

	private Long createdByUserId;

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

	@JsonSetter("imageId")
	public void setImageIdObject(Object imageId) {
		this.imageId = coerceToLong(imageId);
	}

	@JsonSetter("createdByUserId")
	public void setCreatedByUserIdObject(Object createdByUserId) {
		this.createdByUserId = coerceToLong(createdByUserId);
	}

	private Long coerceToLong(Object value) {
		if (value == null) return null;
		if (value instanceof Number) {
			return ((Number) value).longValue();
		}
		if (value instanceof String) {
			try {
				return Long.parseLong(((String) value).trim());
			} catch (NumberFormatException e) {
				return null;
			}
		}
		if (value instanceof java.util.Map) {
			java.util.Map map = (java.util.Map) value;
			// try common keys
			Object id = map.get("id");
			if (id == null) id = map.get("imageId");
			if (id == null) id = map.get("value");
			return coerceToLong(id);
		}
		return null;
	}

}

package com.condigence.nsproductservice.dto;

import java.util.Arrays;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonSetter;

public class BrandDTO {

	private Long id;

	private String name;
	
	private Long imageId;

	private byte[] pic;

	public Long getCreatedByUser() {
		return createdByUser;
	}

	public void setCreatedByUser(Long createdByUser) {
		this.createdByUser = createdByUser;
	}

	private Long createdByUser;

    @JsonSetter("imageId")
    public void setImageIdObject(Object imageId) {
        if (imageId == null) { this.imageId = null; return; }
        if (imageId instanceof Number) { this.imageId = ((Number) imageId).longValue(); return; }
        if (imageId instanceof String) { try { this.imageId = Long.parseLong((String) imageId); return; } catch (NumberFormatException ignored) {} }
        if (imageId instanceof Map) {
            Object id = ((Map<?, ?>) imageId).get("id");
            if (id instanceof Number) { this.imageId = ((Number) id).longValue(); return; }
            if (id instanceof String) { try { this.imageId = Long.parseLong((String) id); return; } catch (NumberFormatException ignored) {} }
        }
    }

    @JsonSetter("createdByUser")
    public void setCreatedByUserObject(Object createdByUser) {
        if (createdByUser == null) { this.createdByUser = null; return; }
        if (createdByUser instanceof Number) { this.createdByUser = ((Number) createdByUser).longValue(); return; }
        if (createdByUser instanceof String) { try { this.createdByUser = Long.parseLong((String) createdByUser); return; } catch (NumberFormatException ignored) {} }
        if (createdByUser instanceof Map) {
            Object id = ((Map<?, ?>) createdByUser).get("id");
            if (id instanceof Number) { this.createdByUser = ((Number) id).longValue(); return; }
            if (id instanceof String) { try { this.createdByUser = Long.parseLong((String) id); return; } catch (NumberFormatException ignored) {} }
        }
    }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getImageId() {
		return imageId;
	}

	public void setImageId(Long imageId) {
		this.imageId = imageId;
	}

	public byte[] getPic() {
		return pic;
	}

	public void setPic(byte[] pic) {
		this.pic = pic;
	}

	@Override
	public String toString() {
		return "BrandDTO [id=" + id + ", name=" + name + ", imageId=" + imageId + ", pic=" + Arrays.toString(pic) + "]";
	}

	

}

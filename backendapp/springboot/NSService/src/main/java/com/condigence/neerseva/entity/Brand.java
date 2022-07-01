package com.condigence.neerseva.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "brand")
@Entity
public class Brand {

	@Id
	@Column(name = "id")
	@GeneratedValue
	private Long id;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "image_id")
	private Long imageId;
	
	
	@Column(name = "date_created")
	private String dateCreated;
	
	
	@Column(name = "created_by_user")
	private Long createdByUser;
	
	@Column(name = "is_deleted")
	private String isDeleted;
	
	

	public String getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(String isDeleted) {
		this.isDeleted = isDeleted;
	}

	@Override
	public String toString() {
		return "Brand [id=" + id + ", name=" + name + ", imageId=" + imageId + ", dateCreated=" + dateCreated
				+ ", createdByUser=" + createdByUser + ", isDeleted=" + isDeleted + "]";
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getImageId() {
		return imageId;
	}

	public void setImageId(Long imageId) {
		this.imageId = imageId;
	}

	public String getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(String dateCreated) {
		this.dateCreated = dateCreated;
	}

	public Long getCreatedByUser() {
		return createdByUser;
	}

	public void setCreatedByUser(Long createdByUser) {
		this.createdByUser = createdByUser;
	}
	


}

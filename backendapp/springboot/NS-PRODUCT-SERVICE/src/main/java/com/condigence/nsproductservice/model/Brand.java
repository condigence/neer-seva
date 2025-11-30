package com.condigence.nsproductservice.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "brand")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
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
	
}

package com.condigence.nsuserservice.entity;


import javax.persistence.*;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	@EqualsAndHashCode.Include
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

    @Column(name = "is_deleted")
    private String isDeleted;

    @Column(name = "is_active")
    private String isActive;

    @Column(name = "date_created")
    private String dateCreated;

    @Column(name = "address_id")
    private Long addressId;

}

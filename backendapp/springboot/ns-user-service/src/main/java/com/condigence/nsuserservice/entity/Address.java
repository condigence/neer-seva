package com.condigence.nsuserservice.entity;

import javax.persistence.*;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "address")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Address {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	@EqualsAndHashCode.Include
	private long id;

	@Column(name = "type")
	private String type;

	@Column(name = "line1")
	private String line1;

	@Column(name = "line2")
	private String line2;

	@Column(name = "line3")
	private String line3;
	
	@Column(name = "line4")
	private String line4;

	@Column(name = "pin")
	private String pin;
	
	@Column(name = "city")
	private String city;
	
	@Column(name = "state")
	private String state;
	
	@Column(name = "country")
	private String country;
	//
	//private String locationLong;
	//private String locationLatt;
	//private String locationName;

	@Column(name = "userId")
	private Long userId;

	private String isDefault;
}

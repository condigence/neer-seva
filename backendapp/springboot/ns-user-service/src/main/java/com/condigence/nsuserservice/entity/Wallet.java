package com.condigence.nsuserservice.entity;


import javax.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "wallet")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Wallet {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	@EqualsAndHashCode.Include
	private Long id;

	@Column(name = "cust_id")
	private Long custId;

	@Column(name = "balance")
	private String balance;

	@Column(name = "is_active")
	private String isActive;

	@Column(name = "last_recharged")
	private String lastRecharged;

}

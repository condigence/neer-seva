package com.condigence.nsuserservice.entity;


import javax.persistence.*;

@Entity
@Table(name = "wallet")
public class Wallet {

	public Wallet() {
		super();
	}

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
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

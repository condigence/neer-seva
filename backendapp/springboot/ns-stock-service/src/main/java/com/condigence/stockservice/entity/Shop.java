package com.condigence.stockservice.entity;



import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "shop")
@Data
@NoArgsConstructor
public class Shop {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long shopId;

	@Column(name = "name")
	private String shopName;

	@Column(name = "shopImageId")
	private Long imageId;

	@Column(name = "type")
	private String shopType;

	@Column(name = "addressId")
	private Long shopAddressId;

	@Column(name = "userId")
	private Long userId;

	@Column(name = "code")
	private String shopCode;

	@Column(name = "branch")
	private String shopBranch;

    @Column(name = "gstin")
    private String gstin;

    @Column(name = "date_created")
    private Date shopDateCreated;

}

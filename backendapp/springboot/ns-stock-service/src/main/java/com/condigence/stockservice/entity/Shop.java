package com.condigence.stockservice.entity;


import jakarta.persistence.*;

@Entity
@Table(name = "shop")
public class Shop {

	public Shop() {
		super();
		// TODO Auto-generated constructor stub
	}

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

	public long getShopId() {
		return shopId;
	}

	public String getShopName() {
		return shopName;
	}

	public Long getImageId() {
		return imageId;
	}

	public String getShopType() {
		return shopType;
	}

	public Long getShopAddressId() {
		return shopAddressId;
	}

	public Long getUserId() {
		return userId;
	}

	public String getShopCode() {
		return shopCode;
	}

	public String getShopBranch() {
		return shopBranch;
	}

	public void setShopId(long shopId) {
		this.shopId = shopId;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public void setImageId(Long imageId) {
		this.imageId = imageId;
	}

	public void setShopType(String shopType) {
		this.shopType = shopType;
	}

	public void setShopAddressId(Long shopAddressId) {
		this.shopAddressId = shopAddressId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public void setShopCode(String shopCode) {
		this.shopCode = shopCode;
	}

	public void setShopBranch(String shopBranch) {
		this.shopBranch = shopBranch;
	}

}

package com.condigence.nsproductservice.dto;

public class DashboardDTO {

	private Long totalUsers;
	private Long totalVendors;
	private Long totalCustomers;
	private Long totalOrders;
	private Long totalSales;
	private Long totalBrands;
	private Long totalItems;
	private Long totalShops;

	public Long getTotalUsers() {
		return totalUsers;
	}

	public Long getTotalVendors() {
		return totalVendors;
	}

	public Long getTotalCustomers() {
		return totalCustomers;
	}

	public Long getTotalOrders() {
		return totalOrders;
	}

	public Long getTotalSales() {
		return totalSales;
	}

	public Long getTotalBrands() {
		return totalBrands;
	}

	public Long getTotalItems() {
		return totalItems;
	}

	public Long getTotalShops() {
		return totalShops;
	}

	public void setTotalUsers(Long totalUsers) {
		this.totalUsers = totalUsers;
	}

	public void setTotalVendors(Long totalVendors) {
		this.totalVendors = totalVendors;
	}

	public void setTotalCustomers(Long totalCustomers) {
		this.totalCustomers = totalCustomers;
	}

	public void setTotalOrders(Long totalOrders) {
		this.totalOrders = totalOrders;
	}

	public void setTotalSales(Long totalSales) {
		this.totalSales = totalSales;
	}

	public void setTotalBrands(Long totalBrands) {
		this.totalBrands = totalBrands;
	}

	public void setTotalItems(Long totalItems) {
		this.totalItems = totalItems;
	}

	public void setTotalShops(Long totalShops) {
		this.totalShops = totalShops;
	}

}

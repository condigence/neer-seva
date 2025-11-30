package com.condigence.nsorderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardDTO {

	private Long totalUsers;
	private Long totalVendors;
	private Long totalCustomers;
	private Long totalOrders;
	private Long totalSales;
	private Long totalBrands;
	private Long totalItems;
	private Long totalShops;
	private Long pendingOrders;
	private Long completedOrders;

}

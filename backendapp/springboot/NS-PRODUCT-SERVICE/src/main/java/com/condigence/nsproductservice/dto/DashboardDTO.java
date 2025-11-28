package com.condigence.nsproductservice.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DashboardDTO {

    private Long totalUsers;
    private Long totalVendors;
    private Long totalCustomers;
    private Long totalOrders;
    private Long totalSales;
    private Long totalBrands;
    private Long totalItems;
    private Long totalShops;

}

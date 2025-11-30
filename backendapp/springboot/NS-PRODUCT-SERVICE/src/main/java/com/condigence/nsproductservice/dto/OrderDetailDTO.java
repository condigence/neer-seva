package com.condigence.nsproductservice.dto;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderDetailDTO {

    private VendorDTO vendor;

    private CustomerDTO customer;

    private List<ItemDTO> items;

}

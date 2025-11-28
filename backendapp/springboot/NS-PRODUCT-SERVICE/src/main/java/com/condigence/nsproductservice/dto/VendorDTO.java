package com.condigence.nsproductservice.dto;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class VendorDTO {

    private Long vendorId;

    private String name;

    private String contact;

    private String email;

    private Long imageId;

    private byte[] pic;

    List<ShopDTO> shopList;

    List<AddressDTO> addressList;

}

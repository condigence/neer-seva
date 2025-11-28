package com.condigence.nsproductservice.dto;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CustomerDTO {

    private Long customerId;

    private String name;

    private String email;

    private String contact;

    private Long imageId;

    private byte[] pic;

    List<AddressDTO> addressList;

}

package com.condigence.nsproductservice.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ShopDTO {

    private long id;

    private String name;

    private Long imageId;

    private byte[] pic;

    private String type;

    private Long addressId;

    private Long userId;

    private String code;

    private String branch;

}

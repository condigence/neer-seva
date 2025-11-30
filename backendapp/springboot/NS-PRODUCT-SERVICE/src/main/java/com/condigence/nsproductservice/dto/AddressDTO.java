package com.condigence.nsproductservice.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AddressDTO {

    private Long id;
    private String type;
    private String line1;
    private Long userId;
    private String isDefault;

    private String line2;
    private String line3;
    private String line4;

    private String pin;
    private String city;
    private String state;
    private String country;

    private String locationLong;
    private String locationLatt;
    private String locationName;

}

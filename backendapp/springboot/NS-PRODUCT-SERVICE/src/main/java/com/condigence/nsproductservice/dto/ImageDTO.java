package com.condigence.nsproductservice.dto;

import java.util.Arrays;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ImageDTO {

    private Long id;

    private String name;

    private Long imageId;

    private byte[] pic;

}

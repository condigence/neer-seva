package com.condigence.imageservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImageDTO {
    private Long id;
    private String name;
    private String imageName; // unique_name
    private Long imageSize;
    private String moduleName;
    private String type;
    private String pic; // base64 string of the image
}

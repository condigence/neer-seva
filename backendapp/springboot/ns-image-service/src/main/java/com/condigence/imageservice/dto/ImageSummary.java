package com.condigence.imageservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImageSummary {
    private Long id;
    private String name;
    private String imageName;
    private Long imageSize;
    private String moduleName;
    private String type;
}

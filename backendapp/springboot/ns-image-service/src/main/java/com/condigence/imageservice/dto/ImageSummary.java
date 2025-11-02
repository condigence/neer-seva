package com.condigence.imageservice.dto;

public class ImageSummary {
    private Long id;
    private String name;
    private String imageName;
    private Long imageSize;
    private String moduleName;
    private String type;

    public ImageSummary() {}

    public ImageSummary(Long id, String name, String imageName, Long imageSize, String moduleName, String type) {
        this.id = id;
        this.name = name;
        this.imageName = imageName;
        this.imageSize = imageSize;
        this.moduleName = moduleName;
        this.type = type;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getImageName() { return imageName; }
    public void setImageName(String imageName) { this.imageName = imageName; }

    public Long getImageSize() { return imageSize; }
    public void setImageSize(Long imageSize) { this.imageSize = imageSize; }

    public String getModuleName() { return moduleName; }
    public void setModuleName(String moduleName) { this.moduleName = moduleName; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
}


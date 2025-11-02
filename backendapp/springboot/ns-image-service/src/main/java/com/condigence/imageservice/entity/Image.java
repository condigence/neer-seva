package com.condigence.imageservice.entity;

import jakarta.persistence.*;
import java.io.Serial;
import java.io.Serializable;

@Table(name = "image")
@Entity
public class Image implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "module_name")
	private String moduleName;
	
	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	@Column(name = "name")
	private String name;

	@Column(name = "type")
	private String type;

	@Column(name = "path", length = 512)
	private String imagePath;

	@Column(name = "size")
	private Long imageSize;

	@Column(name = "unique_name")
	private String imageName;

	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "pic")
	private byte[] pic;

	public Image() {
		// JPA requires a no-arg constructor
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public byte[] getPic() {
		return pic;
	}

	public void setPic(byte[] pic) {
		this.pic = pic;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public Long getImageSize() {
		return imageSize;
	}

	public void setImageSize(Long imageSize) {
		this.imageSize = imageSize;
	}

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	@Override
	public String toString() {
		int picLen = pic == null ? 0 : pic.length;
		return "Image [id=" + id + ", moduleName=" + moduleName + ", name=" + name + ", type=" + type + ", imagePath="
				+ imagePath + ", imageSize=" + imageSize + ", imageName=" + imageName + ", picLength=" + picLen + "]";
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Image image = (Image) o;
		return id != null && id.equals(image.id);
	}

	@Override
	public int hashCode() {
		return id == null ? 0 : id.hashCode();
	}

}

package com.condigence.imageservice.entity;

import jakarta.persistence.*;
import java.util.Arrays;

@Table(name = "image")
@Entity
public class Image {

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

	@Column(name = "path")
	private String imagePath;

	@Column(name = "size")
	private long imageSize;

	@Column(name = "unique_name") /// neerseva Image format : name(0,3)+datTime
	private String imageName;

	@Lob
	@Column(name = "pic")
	private byte[] pic;

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the pic
	 */
	public byte[] getPic() {
		return pic;
	}

	/**
	 * @param pic the pic to set
	 */
	public void setPic(byte[] pic) {
		this.pic = pic;
	}

	/**
	 * @return the imagePath
	 */
	public String getImagePath() {
		return imagePath;
	}

	/**
	 * @param imagePath the imagePath to set
	 */
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	/**
	 * @return the imageSize
	 */
	public long getImageSize() {
		return imageSize;
	}

	/**
	 * @param l the imageSize to set
	 */
	public void setImageSize(long l) {
		this.imageSize = l;
	}

	/**
	 * @return the imageName
	 */
	public String getImageName() {
		return imageName;
	}

	/**
	 * @param imageName the imageName to set
	 */
	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	@Override
	public String toString() {
		return "Image [id=" + id + ", moduleName=" + moduleName + ", name=" + name + ", type=" + type + ", imagePath="
				+ imagePath + ", imageSize=" + imageSize + ", imageName=" + imageName + ", pic=" + Arrays.toString(pic)
				+ "]";
	}

	

}

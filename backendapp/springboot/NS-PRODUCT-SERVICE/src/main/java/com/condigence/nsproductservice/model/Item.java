package com.condigence.nsproductservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "item")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Item {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "name")
	private String name;

	@Column(name = "code")
	private String code;

	@Column(name = "price")
	private Integer price;

	@Column(name = "mrp")
	private Integer mrp;

	@Column(name = "dispPrice")
	private Integer dispPrice;

	@Column(name = "discount")
	private Integer discount;

	@Column(name = "type")
	private String type;

	@Column(name = "description")
	private String description;

	@Column(name = "capacity")
	private Integer capacity;

	@Column(name = "brandId")
	private Long brandId;

	@Column(name = "imageId")
	private Long imageId;

	@Column(name = "dateCreated")
	private Date dateCreated;

	@Column(name = "quantity")
	private Integer quantity;

    @Column(name = "stockId")
    private Long stockId;

    @Column(name = "unit")
    private String unit;

}

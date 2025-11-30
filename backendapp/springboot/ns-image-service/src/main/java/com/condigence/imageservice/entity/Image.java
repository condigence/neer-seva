package com.condigence.imageservice.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import java.io.Serial;
import java.io.Serializable;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "nsimage")
@SuppressWarnings("unused")
@Getter
@Setter
@NoArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Image implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id")
	@SequenceGenerator(name = "hibernate_sequence", sequenceName = "hibernate_sequence", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "hibernate_sequence")
	@EqualsAndHashCode.Include
	@ToString.Include
	private Long id;

	@Column(name = "module_name")
	private String moduleName;

	@Column(name = "name")
	@ToString.Include
	private String name;

	@Column(name = "type")
	@JsonProperty("type")
	private String type;

	@Column(name = "size")
	@JsonProperty("imageSize")
	private Long imageSize;

	@Column(name = "unique_name")
	private String imageName;

	// JPA requires a no-arg constructor; Lombok provides a protected one above

}

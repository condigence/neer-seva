package com.condigence.bookingservice.entity;


import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Table(name = "seat")
public class Seat {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@Column(name = "number")
	private String number;

	@Column(name = "seatType")
	private String seatType;

	@Column(name = "seatStatus")
	private String seatStatus;

	@Column(name = "price", precision = 0, nullable = false)
	private Double price;

	@Column(name = "theaterId")
	private Long theaterId;
	@Column(name = "screenId")
	private Long screenId;
	@Column(name = "movieShowId")
	private Long movieShowId;
	@Column(name = "showId")
	private Long showId;
}

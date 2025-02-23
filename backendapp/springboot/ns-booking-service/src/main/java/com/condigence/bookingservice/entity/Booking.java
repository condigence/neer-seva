package com.condigence.bookingservice.entity;

import com.condigence.bookingservice.dto.ScreenDTO;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "booking")
@Data
public class Booking {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "numberOfSeats")
	private int numberOfSeats;

	@Column(name = "bookingStatus")
	private String bookingStatus;
	@Column(name = "showId")
	private Long showId;

	@Column(name = "theaterId")
	private Long theaterId;

	@Column(name = "screenId")
	private Long screenId;

	@Column(name = "seats")
	String seats; // in CSV format

	@Column(name = "bookingCode")
	private String bookingCode;

	@Column(name = "userId")
	private Long userId;

	@Column(name = "bookingAmount")
	private double bookingAmount;

	@Column(name = "bookingDate", columnDefinition = "DATE", nullable = false)
	private LocalDateTime bookingDate;

}

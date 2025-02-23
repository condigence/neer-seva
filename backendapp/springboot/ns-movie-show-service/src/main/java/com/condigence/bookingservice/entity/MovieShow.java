package com.condigence.bookingservice.entity;


import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Table(name = "movieShow")
public class MovieShow {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@Column(name = "theaterId")
	private Long theaterId;
	@Column(name = "screenId")
	private Long screenId;
	@Column(name = "movieId")
	private Long movieId;

	@Column(name = "cityId")
	private Long cityId;

	@Column(name = "showId")
	private Long showId;

	@Column(name = "showStartDate")
	private LocalDateTime showStartDate;

	@Column(name = "showEndDate")
	private LocalDateTime showEndDate;

}

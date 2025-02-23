package com.condigence.bookingservice.dto;


import lombok.*;

import javax.persistence.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class MovieShowDTO {

	private Long id;

	private Long theaterId;

	private Long screenId;

	private Long movieId;

	private Long cityId;

	private Long showId;

}

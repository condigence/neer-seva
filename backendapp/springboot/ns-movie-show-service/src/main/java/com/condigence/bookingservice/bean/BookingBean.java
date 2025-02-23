package com.condigence.bookingservice.bean;

import com.condigence.bookingservice.dto.SeatDTO;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class BookingBean {

	private Long theaterId;

	private Long screenId;

	private Long movieId;

	private Long showId; // Movie Show Id

	private String seats; // id1, id2, id3

	private LocalDateTime selectedMovieDate;

}

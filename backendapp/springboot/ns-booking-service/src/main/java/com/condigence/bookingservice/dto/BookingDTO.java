package com.condigence.bookingservice.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class BookingDTO {

    private Long id; // Ticket Number

    private int numberOfSeats;

    private String bookingStatus;

    private double bookingAmount;

    private MovieDTO movieDTO;

    private TheaterDTO theaterDTO;

    private ShowDTO showDTO;

    private ScreenDTO screenDTO;

    List<SeatDTO> seatList;

    private String bookingCode;

    private UserDTO userDTO;

    private LocalDateTime bookingDate;

}

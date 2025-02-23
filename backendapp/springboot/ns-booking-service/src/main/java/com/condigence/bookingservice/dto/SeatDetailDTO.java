package com.condigence.bookingservice.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class SeatDetailDTO {

    private Long totalAvailableSeats;

    private String seatsAvailableStatus; // Almost Full, Available, Not Available, Filling Fast

    private List<SeatDTO> seatList;  // Available, Booked, Reserved Seats

}

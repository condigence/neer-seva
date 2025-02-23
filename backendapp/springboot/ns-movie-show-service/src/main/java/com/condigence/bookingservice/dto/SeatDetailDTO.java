package com.condigence.bookingservice.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@ToString
public class SeatDetailDTO {

    private Long totalRequestedSeats;

    private String seatsStatus; // Almost Full, Available, Not Available, Filling Fast

    private List<SeatDTO> seatList;  // Available, Booked, Reserved Seats

}

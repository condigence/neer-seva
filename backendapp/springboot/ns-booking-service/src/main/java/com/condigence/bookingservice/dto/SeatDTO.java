package com.condigence.bookingservice.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SeatDTO {
    private Long id;
    private String number;
    private String seatType;
    private String seatStatus;
    private Double price;
    private Long theaterId;
    private Long screenId;
    private Long showId;
}

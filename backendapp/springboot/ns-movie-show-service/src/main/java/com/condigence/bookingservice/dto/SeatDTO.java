package com.condigence.bookingservice.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;

@Getter
@Setter
@ToString
public class SeatDTO {
    private Long id;
    private String number;

    private Double price;

    private Long screenId;

    private String seatStatus;
    private String seatType;

    private Long showId;
    private Long theaterId;


}

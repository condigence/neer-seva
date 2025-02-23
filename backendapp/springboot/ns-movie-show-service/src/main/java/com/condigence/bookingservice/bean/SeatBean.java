package com.condigence.bookingservice.bean;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SeatBean {
    private Long id;
    private String number;
    private String seatType;
    private String seatStatus;
    private Double price;
    private Long theaterId;
    private Long screenId;
    private Long showId;
}

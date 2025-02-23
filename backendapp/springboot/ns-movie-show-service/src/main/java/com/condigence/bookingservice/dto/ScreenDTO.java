package com.condigence.bookingservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ScreenDTO {

    private Long id;

    private int number; //Hall number like 1,2,3

    private int totalSeats; // number of seats in that Hall  ( row (A,B, C... W) 24) * ( col = 45 (15+15+15)) = 1080


}

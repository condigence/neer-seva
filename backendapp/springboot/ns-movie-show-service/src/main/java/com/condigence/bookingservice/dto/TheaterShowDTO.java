package com.condigence.bookingservice.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@Setter
public class TheaterShowDTO {
    private Long id;
    private String name;
    private String movieName;
    private String city;
    private String screen; // only One for now

    List<ShowTimeDTO> shows;
   // List<MovieShowDTO> movieShows;
}

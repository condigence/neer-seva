package com.condigence.bookingservice.dto;

import lombok.*;

import java.time.LocalDateTime;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Data
public class ShowDTO {
    private Long id;

    private CityDTO city;

    private TheaterDTO theater;

    private ScreenDTO screen;

    private SeatDetailDTO  seatDetail;

    private MovieDTO movie;

    private String name;

    private String showTime;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

}

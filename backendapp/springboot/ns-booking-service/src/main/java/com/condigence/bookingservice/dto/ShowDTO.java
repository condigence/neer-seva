package com.condigence.bookingservice.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ShowDTO {

    private Long id;

    private String name;

    private LocalDateTime startTime;

    private LocalDateTime endTime;
}

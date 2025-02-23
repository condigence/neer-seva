package com.condigence.bookingservice.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
public class MovieDTO {
    private Long id;
    private String title;
    private String description;
    private String genre;
    private String language;
    private Long duration;
    private Long showID;
    private LocalDateTime releaseDate;
}

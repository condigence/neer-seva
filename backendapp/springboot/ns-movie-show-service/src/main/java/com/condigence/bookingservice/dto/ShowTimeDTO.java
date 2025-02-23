package com.condigence.bookingservice.dto;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Data
public class ShowTimeDTO {

    private Long movieShowId;
    private String showTime;

}

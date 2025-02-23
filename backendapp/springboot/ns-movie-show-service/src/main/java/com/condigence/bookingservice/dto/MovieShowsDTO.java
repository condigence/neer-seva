package com.condigence.bookingservice.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MovieShowsDTO {

	private Long id;

	List<MovieDTO> movieDTOList;

	List<ShowDTO> showDTOList;


}

package com.condigence.bookingservice.bean;


import lombok.*;

import javax.persistence.*;
import java.util.Date;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data

public class MovieBean {

	private String title;

	private Long imageId;

	private String genre;

	private String description;

	private Long duration;

	private String language;

	private Date releaseDate;

}

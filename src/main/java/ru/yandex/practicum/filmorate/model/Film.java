package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NonNull;


import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class Film {

	private int id;

	@NotBlank
	private String name;

	@NonNull
	@Size(min = 1, max = 200)
	private String description;

	@NonNull
	private LocalDate releaseDate;

	@NonNull
	private long duration;
	private Set<Integer> listOfLike = new HashSet<>();

	public Film(String name, String description, LocalDate releaseDate, long duration) {
		this.name = name;
		this.description = description;
		this.releaseDate = releaseDate;
		this.duration = duration;

	}
}

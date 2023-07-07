package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NonNull;

import java.time.LocalDate;

@Data
public class Film {
    @Min(1)
    private int id;

    @NotBlank
    private String name;

    @NonNull
    @Size(max = 200)
    private String description;

    @NonNull
    private LocalDate releaseDate;

    @NonNull
    @Positive
    private long duration;



    public Film(String name, String description, LocalDate releaseDate, long duration) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;

    }
}

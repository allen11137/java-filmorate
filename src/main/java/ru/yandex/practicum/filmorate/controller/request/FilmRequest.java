package ru.yandex.practicum.filmorate.controller.request;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class FilmRequest {
    private Integer id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private Integer duration;
    private Rating mpa;
    private List<Genre> genres;

    @Data
    public static class Rating {
        private Integer id;
    }

    @Data
    public static class Genre {
        private Integer id;
    }
}

package ru.yandex.practicum.filmorate.persistence.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name = "FILM")
@NoArgsConstructor
public class Film {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "ID")
    private int id;
    @ManyToMany
    @JoinTable(
            name = "GENRE_FILM",
            joinColumns = { @JoinColumn(name = "film_id") },
            inverseJoinColumns = { @JoinColumn(name = "genre_id") }
    )
    @Column(name = "GENRE")
    private Set<Genre> genres = new HashSet<>();
    @Column(name = "NAME")
    private String name;
    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "RELEASEDATE")
    private LocalDate releaseDate;
    @Column(name = "DURATION")
    private long duration;

    @OneToOne
    @JoinColumn(name = "RATING")
    private Rating mpa;

    public Film(String name, String description, LocalDate releaseDate, long duration) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }
}

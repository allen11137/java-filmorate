package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exception.NotFoundGenreException;
import ru.yandex.practicum.filmorate.persistence.model.Genre;
import ru.yandex.practicum.filmorate.persistence.repository.GenreJdbcRepository;
import ru.yandex.practicum.filmorate.persistence.repository.GenreRepository;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("genres")
@RequiredArgsConstructor
public class ControllerGenre {
    private final GenreJdbcRepository repository;

    @GetMapping("{id}")
    public ResponseEntity<Genre> findById(@PathVariable Integer id) {
        Optional<Genre> genre = repository.findById(id);
        if (genre.isEmpty()) {
            throw new NotFoundGenreException();
        }
        return ResponseEntity.ok(genre.get());
    }

    @GetMapping
    public ResponseEntity<List<Genre>> findAll() {
        return ResponseEntity.ok(repository.findAll());
    }
}

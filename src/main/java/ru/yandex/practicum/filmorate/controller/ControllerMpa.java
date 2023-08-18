package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.persistence.model.Rating;
import ru.yandex.practicum.filmorate.persistence.repository.RatingJdbcRepository;
import ru.yandex.practicum.filmorate.persistence.repository.RatingRepository;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("mpa")
@RequiredArgsConstructor
public class ControllerMpa {
    private final RatingJdbcRepository repository;

    @GetMapping("{id}")
    public ResponseEntity<Rating> findById(@PathVariable Integer id) {
        Optional<Rating> genre = repository.findById(id);
        if (genre.isEmpty()) {
            throw new NotFoundException(id.toString());
        }
        return ResponseEntity.ok(genre.get());
    }

    @GetMapping
    public ResponseEntity<List<Rating>> findById() {
        return ResponseEntity.ok(repository.findAll());
    }
}

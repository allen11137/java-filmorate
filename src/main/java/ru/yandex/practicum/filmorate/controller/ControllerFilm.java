package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.AlreadyObjectExistsException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.ServiceFilm;
import ru.yandex.practicum.filmorate.storage.Film.InMemoryFilmStorage;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;


@Slf4j
@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class ControllerFilm {

	private final ServiceFilm serviceFilm;


	@GetMapping
	public List<Film> getListOfFilms() {
		log.info("Список фильмов: {}", serviceFilm.getListOfFilms().size());
		return new ArrayList<>(serviceFilm.getListOfFilms());
	}

	@PostMapping
	public ResponseEntity<Film> makeFilm(@Valid @RequestBody Film film) {
		serviceFilm.addToFilm(serviceFilm.verifyParametrOfFilm(film));
		return ResponseEntity.status(HttpStatus.OK).body(film);
	}

	@PutMapping
	public ResponseEntity<Film> filmUpdate(@Valid @RequestBody Film film) {
		serviceFilm.updateFilm(film);
		return ResponseEntity.status(HttpStatus.OK).body(film);
	}

	@GetMapping("/{filmId}")
	public ResponseEntity<Film> lookForFilm(@PathVariable long filmId) {
		return ResponseEntity.status(HttpStatus.OK).body(serviceFilm.getOfIdFilm(filmId));
	}


	@PutMapping("/{id}/like/{userId}")
	public ResponseEntity<Film> favouriteFilms(@PathVariable long id, @PathVariable long userId) {
		return ResponseEntity.status(HttpStatus.OK).body(serviceFilm.joinLikeToFilm(id, userId));
	}


	@DeleteMapping("/{id}/like/{userId}")
	public ResponseEntity<Film> deleteLikeInFilm(@PathVariable long id, @PathVariable long userId) {
		return ResponseEntity.status(HttpStatus.OK).body(serviceFilm.deleteToLike(id, userId));
	}

	@GetMapping("/popular")
	public ResponseEntity<List<Film>> getFavoriteFilm(@RequestParam(defaultValue = "10", required = false) int count) {
		return ResponseEntity.status(HttpStatus.OK).body(serviceFilm.getListOfLovelyFilms(count));
	}
}


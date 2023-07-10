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
		log.info("Список фильмов: {}", serviceFilm.amountOfFilm);
		return new ArrayList<>(serviceFilm.amountOfFilm);
	}

	@PostMapping
	public ResponseEntity<Film> makeFilm(@Valid @RequestBody Film film) throws ValidationException, AlreadyObjectExistsException {
		if (!serviceFilm.amountOfFilm.contains(film)) {
			serviceFilm.addToFilm(serviceFilm.verifyParametrOfFilm(film));
			return ResponseEntity.status(HttpStatus.OK).body(film);
		} else {
			throw new AlreadyObjectExistsException("Фильм уже в списке");
		}
	}

	@PutMapping
	public ResponseEntity<Film> filmUpdate(@Valid @RequestBody Film film) throws ValidationException, NotFoundException {
		if (serviceFilm.verifyOptionsOfFilm(film)) {
			log.info("Фильм обновлён: {}", film);
			serviceFilm.updateOptionsOfFilm(film);
			return ResponseEntity.status(HttpStatus.OK).body(film);
		} else {
			throw new NotFoundException("Фильм не найден" + film.getId());
		}
	}


}


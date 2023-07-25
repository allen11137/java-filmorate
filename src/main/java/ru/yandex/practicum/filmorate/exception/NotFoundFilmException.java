package ru.yandex.practicum.filmorate.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NotFoundFilmException extends RuntimeException {
	public NotFoundFilmException(long idOfFilm) {
		super(String.format("Фильм не найден", idOfFilm));
	}
}
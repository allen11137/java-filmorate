package ru.yandex.practicum.filmorate.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundUserException extends RuntimeException {
	public NotFoundUserException(long idOfUser) {
		super(String.format("Пользователь не найден %s", idOfUser));
	}
}
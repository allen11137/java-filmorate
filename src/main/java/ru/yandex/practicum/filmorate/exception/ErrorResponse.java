package ru.yandex.practicum.filmorate.exception;

public class ErrorResponse {
	// название ошибки
	String error;
	String message;

	public ErrorResponse(String error, String message) {
		this.error = error;
		this.message = message;
	}

	public String getError() {
		return error;
	}

	public String getMessage() {
		return message;
	}
}
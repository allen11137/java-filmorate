package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Map;

public interface FilmStorage {
	void addToFilm(Film film);

	void updateFilm(Film film);

	void deleteToFilm(Film film);

	Map<Integer, Film> getListOfFilms();
}

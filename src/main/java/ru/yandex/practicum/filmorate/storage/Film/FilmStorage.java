package ru.yandex.practicum.filmorate.storage.Film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Set;

public interface FilmStorage {

	void addToFilm(Film film);

	void updateFilm(Film film);

	void deleteToFilm(Film film);

	Set<Film> getListOfFilms();
}

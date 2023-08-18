package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.controller.request.FilmRequest;
import ru.yandex.practicum.filmorate.persistence.model.Film;
import ru.yandex.practicum.filmorate.persistence.model.Genre;
import ru.yandex.practicum.filmorate.persistence.model.Rating;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface FilmStorage {
	void addToFilm(Film film);

	Film updateFilm(FilmRequest filmRequest);

	void deleteToFilm(Film film);

	Map<Integer, Film> getListOfFilms();

	List<Film> getListFavoriteFilms(int amount);

	void addLikeToFilm(int filmId, int idOfUser);

	void removeLike(int filmId, int idOfUser);

    Rating getMpas(Integer mpa);

	Set<Genre> getGenres(List<Integer> idsGenre);
}

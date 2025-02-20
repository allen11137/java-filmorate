package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.AlreadyObjectExistsException;
import ru.yandex.practicum.filmorate.exception.NotFoundFilmException;
import ru.yandex.practicum.filmorate.exception.NotFoundUserException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ServiceFilm implements FilmStorage {
	public static final LocalDate FIRST_DATE_OF_RELEASE = LocalDate.of(1895, 12, 28);
	public static final int SIZE_OF_DESCRIPTION = 200;

	private final FilmStorage inMemoryFilmStorage;

	@Override
	public void addToFilm(Film film) {
		if (!verifyOptionsOfFilm(film)) {
			inMemoryFilmStorage.addToFilm(film);
			log.info("Фильм добавлен: {}", film);
		} else {
			throw new AlreadyObjectExistsException(String.format("Фильм уже %s был добавлен", film.getName()));
		}
	}

	@Override
	public void updateFilm(Film film) {
		if (verifyOptionsOfFilm(film)) {
			inMemoryFilmStorage.updateFilm(film);
			log.info("База фильмов обновлена: {}", film);
		} else {
			throw new NotFoundFilmException(film.getId());
		}
	}

	@Override
	public void deleteToFilm(Film film) {
		if (verifyOptionsOfFilm(film)) {
			inMemoryFilmStorage.deleteToFilm(film);
		} else {
			throw new NotFoundFilmException(film.getId());
		}
	}

	@Override
	public Map<Integer, Film> getListOfFilms() {
		return inMemoryFilmStorage.getListOfFilms();
	}

	public Film getOfIdFilm(int id) {
		if (getListOfFilms().containsKey(id)) {
			return getListOfFilms().get(id);
		} else {
			throw new NotFoundFilmException(id);
		}
	}

	public boolean verifyOptionsOfFilm(Film film) {
		return getListOfFilms().containsKey(film.getId());
	}

	public List<Film> getListOfLovelyFilms(int amount) {
		List<Film> fs = getListOfFilms().values().stream()
				.sorted(Comparator.comparingInt(g -> g.getListOfLike().size()))
				.collect(Collectors.toList());
		Collections.reverse(fs);
		log.info("getListOfLovelyFilms {}", fs);
		return fs.stream().limit(Math.min(getListOfFilms().size(), amount)).collect(Collectors.toList());
	}

	public Film joinLikeToFilm(int filmId, int idOfUser) {
		getOfIdFilm(idOfUser);
		getOfIdFilm(filmId).getListOfLike().add(idOfUser);
		log.info("joinLikeToFilm {}", getOfIdFilm(filmId));
		return getOfIdFilm(filmId);
	}

	public Film deleteToLike(int filmId, int idOfUser) {
		if (getOfIdFilm(filmId).getListOfLike().contains(idOfUser)) {
			getOfIdFilm(filmId).getListOfLike().remove(idOfUser);
			log.info("Отметка мне нравится удалена filmId {}, idOfUser {}", filmId, idOfUser);
			return getOfIdFilm(filmId);
		} else {
			throw new NotFoundUserException(idOfUser);
		}
	}


	public Film verifyParametrOfFilm(Film film) throws ValidationException {
		if (film.getName() == null || film.getName().isBlank()) {
			throw new ValidationException("Имя не должно быть пустым");
		} else if (film.getDescription().length() > SIZE_OF_DESCRIPTION) {
			throw new ValidationException("Слишком длинное описание, максимальное количество символов 200");
		} else if (film.getReleaseDate().isBefore(FIRST_DATE_OF_RELEASE)) {
			throw new ValidationException("Неправильная дата релиза фильма.");
		} else if (film.getDuration() < 1) {
			throw new ValidationException("Неправильная длительность фильма");
		} else {
			return film;
		}
	}

}

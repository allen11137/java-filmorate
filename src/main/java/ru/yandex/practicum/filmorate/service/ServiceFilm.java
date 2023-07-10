package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
public class ServiceFilm {
	public static final LocalDate FIRST_DATE_OF_RELEASE = LocalDate.of(1895, 12, 28);
	public static final int SIZE_OF_DESCRIPTION = 200;

	public final AtomicInteger filmId = new AtomicInteger();
	public final Set<Film> amountOfFilm = new HashSet<>();

	public Film verifyParametrOfFilm(Film film) throws ValidationException {
		if (film.getName() == null || film.getName().isBlank()) {
			throw new ValidationException("Неправильное имя файла");
		} else if (film.getDescription().length() > SIZE_OF_DESCRIPTION) {
			throw new ValidationException("Слишком длинное описание, максимальное количество символов 200");
		} else if (film.getReleaseDate().isBefore(FIRST_DATE_OF_RELEASE)) {
			throw new ValidationException("Неправильная дата релиза фильма.");
		} else if (film.getDuration() < 0) {
			throw new ValidationException("Неправильная длительность фильма");
		} else {
			return film;
		}
	}

	public void updateOptionsOfFilm(Film film) {
		amountOfFilm.forEach(a -> updateFilm(film, a));
	}

	private static void updateFilm(Film film, Film a) {
		if (a.getId() == film.getId()) {
			a.setName(film.getName());
			a.setDescription(film.getDescription());
			a.setReleaseDate(film.getReleaseDate());
			a.setDuration(film.getDuration());
		log.info("База данных обновлена: {}", film);
		}
	}

	public void addToFilm(Film film) {
		filmId.getAndIncrement();
		film.setId(filmId.get());
		log.info("Фильм добавлен: {}", film);
		amountOfFilm.add(film);
	}

	public boolean verifyOptionsOfFilm(Film film) {
		return amountOfFilm.stream().anyMatch(a -> a.getId() == film.getId());
	}

}

package ru.yandex.practicum.filmorate.storage.Film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class InMemoryFilmStorage implements FilmStorage {

	public static final AtomicInteger filmId = new AtomicInteger();
	public final Map<Integer, Film> amountOfFilm = new HashMap<>();

	static {
		filmId.set(1);
	}

	@Override
	public void addToFilm(Film film) {
		int andIncrement = filmId.getAndIncrement();
		film.setId(andIncrement);
		amountOfFilm.put(andIncrement, film);
	}

	@Override
	public void updateFilm(Film film) {
		if (amountOfFilm.containsKey(film.getId())) {
			Film film1 = amountOfFilm.get(filmId);
			film1.setName(film.getName());
			film1.setDescription(film.getDescription());
			film1.setReleaseDate(film.getReleaseDate());
			film1.setDuration(film.getDuration());
		}
	}

	@Override
	public void deleteToFilm(Film film) {
		amountOfFilm.remove(film);
	}

	@Override
	public Map<Integer, Film> getListOfFilms() {
		return amountOfFilm;
	}
}

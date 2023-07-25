package ru.yandex.practicum.filmorate.storage.Film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class InMemoryFilmStorage implements FilmStorage {

	public static final AtomicInteger filmId = new AtomicInteger();
	public final Set<Film> amountOfFilm = new HashSet<>();

	static {
		filmId.set(1);
	}

	@Override
	public void addToFilm(Film film) {
		int andIncrement = filmId.getAndIncrement();
		film.setId(andIncrement);
		amountOfFilm.add(film);
	}

	@Override
	public void updateFilm(Film film) {
		amountOfFilm.forEach(a -> {
			if (a.getId() == film.getId()) {
				a.setName(film.getName());
				a.setDescription(film.getDescription());
				a.setReleaseDate(film.getReleaseDate());
				a.setDuration(film.getDuration());
			}
		});
	}

	@Override
	public void deleteToFilm(Film film) {
		amountOfFilm.remove(film);
	}

	@Override
	public Set<Film> getListOfFilms() {
		return amountOfFilm;
	}
}
